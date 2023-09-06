package com.mjpancheri.financialcontrol.application.service;

import com.mjpancheri.financialcontrol.application.exception.IncomeFromAnotherUserException;
import com.mjpancheri.financialcontrol.application.exception.InvalidIntervalException;
import com.mjpancheri.financialcontrol.application.exception.ResourceNotFoundException;
import com.mjpancheri.financialcontrol.application.exception.UserNotFoundException;
import com.mjpancheri.financialcontrol.application.util.DateUtil;
import com.mjpancheri.financialcontrol.domain.summary.IncomeSummary;
import com.mjpancheri.financialcontrol.domain.summary.dto.CreateIncomeSummaryDTO;
import com.mjpancheri.financialcontrol.domain.user.User;
import com.mjpancheri.financialcontrol.infrastructure.persistence.IncomeSummaryRepositoy;
import com.mjpancheri.financialcontrol.infrastructure.persistence.UserRepository;
import com.mjpancheri.financialcontrol.infrastructure.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class IncomeSummaryService {

    private static final String ERROR_UPDATE_INCOME_SUMMARY = "Error update IncomeSummary with: ";
    private static final String ERROR_UPDATE_INCOME_SUMMARY_UNKNOW_USER = "Error update IncomeSummary with unknow user";
    private static final String ERROR_UPDATE_INCOME_SUMMARY_FORBIDDEN_USER = "Error update IncomeSummary with forbidden user: ";

    private final IncomeSummaryRepositoy incomeSummaryRepositoy;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public List<IncomeSummary> listAllByInterval(String authHeader, LocalDate begin, LocalDate end) {
        if (begin == null || end == null || end.isBefore(begin)) {
            log.error("Error listAllByInterval with invalid interval: " + begin + " | " + end);
            throw new InvalidIntervalException(begin, end);
        }

        String token = tokenService.extractToken(authHeader);
        String email = tokenService.validateToken(token);
        if (email == null) {
            log.error("Error listAllByInterval with unknow user: " + token);
            throw new UserNotFoundException(token);
        }
        User user = (User) userRepository.findByEmail(email);

        return incomeSummaryRepositoy.listAllByUserAndInterval(user, begin, end);
    }

    public List<IncomeSummary> listAllByMonth(String authHeader, String month) {
        if (!DateUtil.isYearMonthPatterCorrect(month)) {
            log.error("Error listAllByMonth with invalid month: " + month);
            throw new InvalidIntervalException(month);
        }

        return listAllByInterval(authHeader, DateUtil.getMonthFirstDay(month), DateUtil.getMonthLastDay(month));
    }

    public IncomeSummary create(String authHeader, CreateIncomeSummaryDTO data) {
        String token = tokenService.extractToken(authHeader);
        String email = tokenService.validateToken(token);
        if (email == null) {
            log.error("Error create IncomeSummary with unknow user");
            throw new UserNotFoundException();
        }
        User user = (User) userRepository.findByEmail(email);

        IncomeSummary incomeSummary = new IncomeSummary(user, data.title(), data.description(),
                data.type(), data.category(), data.amount(), data.date());

        return incomeSummaryRepositoy.save(incomeSummary);
    }

    public IncomeSummary update(String authHeader, String id, CreateIncomeSummaryDTO data) {
        var foundSummary = incomeSummaryRepositoy.findById(UUID.fromString(id));
        if (foundSummary.isEmpty()) {
            log.error(ERROR_UPDATE_INCOME_SUMMARY + id + " | " + data);
            throw new ResourceNotFoundException();
        }

        String token = tokenService.extractToken(authHeader);
        String email = tokenService.validateToken(token);
        if (email == null) {
            log.error(ERROR_UPDATE_INCOME_SUMMARY_UNKNOW_USER);
            throw new UserNotFoundException();
        }
        User user = (User) userRepository.findByEmail(email);
        IncomeSummary incomeSummary = foundSummary.get();

        if (!incomeSummary.getUser().equals(user)) {
            log.error(ERROR_UPDATE_INCOME_SUMMARY_FORBIDDEN_USER + user.getId() + " | " + incomeSummary.getUser().getId());
            throw new IncomeFromAnotherUserException();
        }
        incomeSummary.setTitle(data.title());
        incomeSummary.setDescription(data.description());
        incomeSummary.setType(data.type());
        incomeSummary.setCategory(data.category());
        incomeSummary.setAmount(data.amount());
        incomeSummary.setDate(data.date());

        return incomeSummaryRepositoy.save(incomeSummary);
    }

    public IncomeSummary get(String authHeader, String id) {
        var foundSummary = incomeSummaryRepositoy.findById(UUID.fromString(id));
        if (foundSummary.isEmpty()) {
            log.error(ERROR_UPDATE_INCOME_SUMMARY + id);
            throw new ResourceNotFoundException();
        }

        String token = tokenService.extractToken(authHeader);
        String email = tokenService.validateToken(token);
        if (email == null) {
            log.error(ERROR_UPDATE_INCOME_SUMMARY_UNKNOW_USER);
            throw new UserNotFoundException();
        }
        User user = (User) userRepository.findByEmail(email);
        IncomeSummary incomeSummary = foundSummary.get();

        if (!incomeSummary.getUser().equals(user)) {
            log.error(ERROR_UPDATE_INCOME_SUMMARY_FORBIDDEN_USER + user.getId() + " | " + incomeSummary.getUser().getId());
            throw new IncomeFromAnotherUserException();
        }

        return incomeSummary;
    }

    public void delete(String authHeader, String id) {
        var foundSummary = incomeSummaryRepositoy.findById(UUID.fromString(id));
        if (foundSummary.isEmpty()) {
            log.error(ERROR_UPDATE_INCOME_SUMMARY + id);
            throw new ResourceNotFoundException();
        }

        String token = tokenService.extractToken(authHeader);
        String email = tokenService.validateToken(token);
        if (email == null) {
            log.error(ERROR_UPDATE_INCOME_SUMMARY_UNKNOW_USER);
            throw new UserNotFoundException();
        }
        User user = (User) userRepository.findByEmail(email);
        IncomeSummary incomeSummary = foundSummary.get();

        if (!incomeSummary.getUser().equals(user)) {
            log.error(ERROR_UPDATE_INCOME_SUMMARY_FORBIDDEN_USER + user.getId() + " | " + incomeSummary.getUser().getId());
            throw new IncomeFromAnotherUserException();
        }

        incomeSummaryRepositoy.delete(incomeSummary);
    }
}
