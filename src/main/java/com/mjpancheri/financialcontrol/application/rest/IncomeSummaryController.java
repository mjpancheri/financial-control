package com.mjpancheri.financialcontrol.application.rest;

import com.mjpancheri.financialcontrol.application.service.IncomeSummaryService;
import com.mjpancheri.financialcontrol.domain.summary.IncomeSummary;
import com.mjpancheri.financialcontrol.domain.summary.dto.CreateIncomeSummaryDTO;
import com.mjpancheri.financialcontrol.domain.summary.dto.IncomeSummaryResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/income-summary")
public class IncomeSummaryController {

    @Value("${api.host}")
    private String host;

    private final IncomeSummaryService service;

    @PostMapping
    public ResponseEntity<IncomeSummaryResponseDTO> create(@RequestHeader("Authorization") String authorization,
                                                           @RequestBody @Valid CreateIncomeSummaryDTO body)
            throws URISyntaxException {
        var summary = service.create(authorization, body);

        return ResponseEntity.created(new URI(host + "/api/v1/income-summary/" + summary.getId())).body(summary.convertTo());
    }

    @PutMapping("{id}")
    public ResponseEntity<IncomeSummaryResponseDTO> update(@RequestHeader("Authorization") String authorization,
                                                           @PathVariable("id") @UUID String id,
                                                           @RequestBody @Valid CreateIncomeSummaryDTO body) {
        var summary = service.update(authorization, id, body);

        return ResponseEntity.ok(summary.convertTo());
    }

    @GetMapping("{id}")
    public ResponseEntity<IncomeSummaryResponseDTO> getOne(@RequestHeader("Authorization") String authorization,
                                                           @PathVariable("id") @UUID String id) {
        var summary = service.get(authorization, id);

        return ResponseEntity.ok(summary.convertTo());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<IncomeSummaryResponseDTO> deleteOne(@RequestHeader("Authorization") String authorization,
                                                              @PathVariable("id") @UUID String id) {
        service.delete(authorization, id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list/{begin}/{end}")
    public ResponseEntity<List<IncomeSummaryResponseDTO>> listAllByInterval(@RequestHeader("Authorization") String authorization,
                                                                            @PathVariable("begin")LocalDate begin,
                                                                            @PathVariable("end") LocalDate end) {
        List<IncomeSummary> list = service.listAllByInterval(authorization, begin, end);

        return ResponseEntity.ok(list.stream()
                .map(IncomeSummary::convertTo).toList());
    }

    @GetMapping("/list/{month}")
    public ResponseEntity<List<IncomeSummaryResponseDTO>> listAllByMonth(@RequestHeader("Authorization") String authorization,
                                                                         @PathVariable String month) {
        List<IncomeSummary> list = service.listAllByMonth(authorization, month);

        return ResponseEntity.ok(list.stream()
                .map(IncomeSummary::convertTo).toList());
    }
}
