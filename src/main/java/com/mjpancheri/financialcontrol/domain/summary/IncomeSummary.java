package com.mjpancheri.financialcontrol.domain.summary;

import com.mjpancheri.financialcontrol.domain.summary.dto.IncomeSummaryResponseDTO;
import com.mjpancheri.financialcontrol.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "income_summaries")
public class IncomeSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotBlank
    @Size(min = 3, max = 150)
    @Column(name = "title")
    private String title;

    @Size(min = 3, max = 300)
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private IncomeType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private IncomeCategory category;

    @Positive
    @Column(name = "amount")
    private BigDecimal amount;

    @NotNull
    @Column(name = "date")
    LocalDate date;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public IncomeSummary(User user, String title, String description, IncomeType type, IncomeCategory category, BigDecimal amount, LocalDate date) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public IncomeSummaryResponseDTO convertTo() {
        return new IncomeSummaryResponseDTO(this.id, this.title, this.description, this.type.getType(),
                this.category.getCategory(), this.amount.doubleValue(), this.date, this.createdAt, this.updatedAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IncomeSummary that)) return false;

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return "IncomeSummary{" +
                "id=" + id +
                ", user=" + user +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", amount=" + amount +
                ", date=" + date +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
