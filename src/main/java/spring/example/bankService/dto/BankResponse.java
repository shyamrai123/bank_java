package spring.example.bankService.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankResponse {

    private String responseCode;
    private String responseMessage;
    private AccountInfo accountInfo;
    private LoanRequest loanInfo;
}
