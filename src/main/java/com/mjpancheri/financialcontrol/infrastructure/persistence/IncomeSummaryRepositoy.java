package com.mjpancheri.financialcontrol.infrastructure.persistence;

import com.mjpancheri.financialcontrol.domain.summary.IncomeSummary;
import com.mjpancheri.financialcontrol.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface IncomeSummaryRepositoy extends JpaRepository<IncomeSummary, UUID> {

    @Query("select T from IncomeSummary T where user = :user and date >= :begin and date <= :end")
    List<IncomeSummary> listAllByUserAndInterval(User user, LocalDate begin, LocalDate end);
}
