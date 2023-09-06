package com.mjpancheri.financialcontrol.application.rest;

import com.mjpancheri.financialcontrol.application.exception.FinancialControlExceptionHandler;
import com.mjpancheri.financialcontrol.application.exception.IncomeFromAnotherUserException;
import com.mjpancheri.financialcontrol.application.exception.ResourceNotFoundException;
import com.mjpancheri.financialcontrol.application.exception.UserNotFoundException;
import com.mjpancheri.financialcontrol.application.service.IncomeSummaryService;
import com.mjpancheri.financialcontrol.application.util.CustomUtil;
import com.mjpancheri.financialcontrol.application.util.faker.FakeBuilder;
import com.mjpancheri.financialcontrol.domain.summary.dto.CreateIncomeSummaryDTO;
import com.mjpancheri.financialcontrol.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class IncomeSummaryControllerTest {

    private static final String ENDPOINT_URL = "/api/v1/income-summary";
    private static final String VALID_TOKEN = "VALID-TOKEN";
    private static final String INVALID_TOKEN = "INVALID-TOKEN";
    private static final String ID = UUID.randomUUID().toString();
    private User user;

    @Mock
    private IncomeSummaryService service;

    @InjectMocks
    private IncomeSummaryController controller;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new FinancialControlExceptionHandler(CustomUtil.getMessageSource())).build();

        user = FakeBuilder.buildUser("Jonh Due");
    }

    @Nested
    class CreateTests {
        @Test
        void testReturnCreatedWhenPayloadIsOk() throws Exception {
            when(service.create(anyString(), any(CreateIncomeSummaryDTO.class)))
                    .thenReturn(FakeBuilder.buildIncomeSummary(user));

            mockMvc.perform(
                    post(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", VALID_TOKEN)
                            .content(CustomUtil.asJsonString(FakeBuilder.buildCreateIncomeSummaryDTO()))
            ).andExpect(status().isCreated());
            verify(service, times(1)).create(anyString(), any(CreateIncomeSummaryDTO.class));
            verifyNoMoreInteractions(service);
        }

        @Test
        void testReturnBadRequestWhenTokenIsMissing() throws Exception {
            when(service.create(anyString(), any(CreateIncomeSummaryDTO.class)))
                    .thenReturn(FakeBuilder.buildIncomeSummary(user));

            mockMvc.perform(
                    post(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON)
                            .content(CustomUtil.asJsonString(FakeBuilder.buildCreateIncomeSummaryDTO()))
            ).andExpect(status().isBadRequest());
        }

        @Test
        void testReturnNotFoundWhenTokenIsInvalid() throws Exception {
            when(service.create(anyString(), any(CreateIncomeSummaryDTO.class)))
                    .thenThrow(new UserNotFoundException());

            mockMvc.perform(
                    post(ENDPOINT_URL).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", INVALID_TOKEN)
                            .content(CustomUtil.asJsonString(FakeBuilder.buildCreateIncomeSummaryDTO()))
            ).andExpect(status().isNotFound());
        }
    }

    @Nested
    class UpdateTests {
        @Test
        void testReturnOkWhenPayloadIsCorrect() throws Exception {
            when(service.update(anyString(), anyString(), any(CreateIncomeSummaryDTO.class)))
                    .thenReturn(FakeBuilder.buildIncomeSummary(user));

            mockMvc.perform(
                    put(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", VALID_TOKEN)
                            .content(CustomUtil.asJsonString(FakeBuilder.buildCreateIncomeSummaryDTO()))
            ).andExpect(status().isOk());
            verify(service, times(1)).update(anyString(), anyString(), any(CreateIncomeSummaryDTO.class));
            verifyNoMoreInteractions(service);
        }

        @Test
        void testReturnNotFoundWhenIdIsInvalid() throws Exception {
            when(service.update(anyString(), anyString(), any(CreateIncomeSummaryDTO.class)))
                    .thenThrow(new ResourceNotFoundException());

            mockMvc.perform(
                    put(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", VALID_TOKEN)
                            .content(CustomUtil.asJsonString(FakeBuilder.buildCreateIncomeSummaryDTO()))
            ).andExpect(status().isNotFound());
        }

        @Test
        void testReturnNotFoundWhenTokenIsInvalid() throws Exception {
            when(service.update(anyString(), anyString(), any(CreateIncomeSummaryDTO.class)))
                    .thenThrow(new UserNotFoundException());

            mockMvc.perform(
                    put(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", INVALID_TOKEN)
                            .content(CustomUtil.asJsonString(FakeBuilder.buildCreateIncomeSummaryDTO()))
            ).andExpect(status().isNotFound());
        }

        @Test
        void testReturnBadRequestWhenIdBelongsToAnotherUser() throws Exception {
            when(service.update(anyString(), anyString(), any(CreateIncomeSummaryDTO.class)))
                    .thenThrow(new IncomeFromAnotherUserException());

            mockMvc.perform(
                    put(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", VALID_TOKEN)
                            .content(CustomUtil.asJsonString(FakeBuilder.buildCreateIncomeSummaryDTO()))
            ).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class GetTests {
        @Test
        void testReturnOkWhenIdIsCorrect() throws Exception {
            when(service.get(anyString(), anyString()))
                    .thenReturn(FakeBuilder.buildIncomeSummary(user));

            mockMvc.perform(
                    get(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", VALID_TOKEN)
            ).andExpect(status().isOk());
            verify(service, times(1)).get(anyString(), anyString());
            verifyNoMoreInteractions(service);
        }

        @Test
        void testReturnNotFoundWhenIdIsInvalid() throws Exception {
            when(service.get(anyString(), anyString()))
                    .thenThrow(new ResourceNotFoundException());

            mockMvc.perform(
                    get(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", VALID_TOKEN)
            ).andExpect(status().isNotFound());
        }

        @Test
        void testReturnNotFoundWhenTokenIsInvalid() throws Exception {
            when(service.get(anyString(), anyString()))
                    .thenThrow(new UserNotFoundException());

            mockMvc.perform(
                    get(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", INVALID_TOKEN)
            ).andExpect(status().isNotFound());
        }

        @Test
        void testReturnBadRequestWhenIdBelongsToAnotherUser() throws Exception {
            when(service.get(anyString(), anyString()))
                    .thenThrow(new IncomeFromAnotherUserException());

            mockMvc.perform(
                    get(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", VALID_TOKEN)
            ).andExpect(status().isBadRequest());
        }
    }

    @Nested
    class DeleteTests {
        @Test
        void testReturnNoContentWhenIdIsCorrect() throws Exception {
            doNothing().when(service).delete(anyString(), anyString());

            mockMvc.perform(
                    delete(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", VALID_TOKEN)
            ).andExpect(status().isNoContent());
            verify(service, times(1)).delete(anyString(), anyString());
            verifyNoMoreInteractions(service);
        }

        @Test
        void testReturnNotFoundWhenIdIsInvalid() throws Exception {
            doThrow(new ResourceNotFoundException())
                    .when(service).delete(anyString(), anyString());

            mockMvc.perform(
                    delete(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", VALID_TOKEN)
            ).andExpect(status().isNotFound());
        }

        @Test
        void testReturnNotFoundWhenTokenIsInvalid() throws Exception {
            doThrow(new UserNotFoundException())
                    .when(service).delete(anyString(), anyString());

            mockMvc.perform(
                    delete(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", INVALID_TOKEN)
            ).andExpect(status().isNotFound());
        }

        @Test
        void testReturnBadRequestWhenIdBelongsToAnotherUser() throws Exception {
            doThrow(new IncomeFromAnotherUserException())
                    .when(service).delete(anyString(), anyString());

            mockMvc.perform(
                    delete(ENDPOINT_URL + "/" + ID).contentType(MediaType.APPLICATION_JSON)
                            .header("Authorization", VALID_TOKEN)
            ).andExpect(status().isBadRequest());
        }
    }
}