package spring.example.bankService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.example.bankService.dto.*;
import spring.example.bankService.service.LoanService;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    // Endpoint to apply for a loan
    @PostMapping("/apply")
    public ResponseEntity<BankResponse> applyForLoan(@RequestBody LoanRequest loanRequest) {
        BankResponse response = loanService.applyForLoan(loanRequest);
        return ResponseEntity.ok(response);
    }

    // Endpoint to get loans by account number
    @GetMapping("/{accountNumber}")
    public ResponseEntity<List<LoanRequest>> getLoansByAccountNumber(@PathVariable String accountNumber) {
        List<LoanRequest> loans = loanService.getLoansByAccountNumber(accountNumber);
        return ResponseEntity.ok(loans);
    }

    // Endpoint to repay a loan
    @PostMapping("/repay")
    public ResponseEntity<BankResponse> repayLoan(@RequestBody LoanRepaymentRequest repaymentRequest) {
        BankResponse response = loanService.repayLoan(repaymentRequest);
        return ResponseEntity.ok(response);
    }
}
