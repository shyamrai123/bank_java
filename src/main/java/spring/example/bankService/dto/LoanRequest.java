package spring.example.bankService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequest {
    private Long loanId;
    private String accountNumber;
    private BigDecimal loanAmount;
    private BigDecimal loanBalance;
    private BigDecimal interestRate;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Integer tenureMonths = 12; // Default to 12 months
    private  String  status;
    private  String  email;

}
