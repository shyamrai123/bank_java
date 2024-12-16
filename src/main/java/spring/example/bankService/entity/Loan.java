package spring.example.bankService.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Data
@Entity
@Table(name = "loan_table")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal loanAmount;

    @Column(name = "loan_balance", unique = true)
    private BigDecimal loanBalance;

    @Column(nullable = false)
    private BigDecimal interestRate;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer tenureMonths = 12; // Default to 12 months

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

}
