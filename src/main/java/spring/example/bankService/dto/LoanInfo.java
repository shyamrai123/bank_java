package spring.example.bankService.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "loanInfoBuilder")
public class LoanInfo extends LoanRequest {
    private BigDecimal loanAmount;
    private BigDecimal loanBalance;
    private BigDecimal interestRate;
    private String accountNumber;
    private LocalDate startDate;
    private LocalDate dueDate;
    private String status;
}


