package spring.example.bankService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import spring.example.bankService.dto.*;
import spring.example.bankService.entity.Account;
import spring.example.bankService.entity.Loan;
import spring.example.bankService.service.AccountService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // Endpoint to create a new account
    @PostMapping("/create")
    public ResponseEntity<BankResponse> createAccount(@RequestBody AccountRequest accountRequest) {
        BankResponse response = accountService.createAccount(accountRequest);
        return ResponseEntity.ok(response);
    }

    // Endpoint to credit an account
    @PostMapping("/credit")
    public ResponseEntity<BankResponse> creditAccount(@RequestBody CreditDebitRequest creditRequest) {
        BankResponse response = accountService.creditAccount(creditRequest);
        return ResponseEntity.ok(response);
    }

    // Endpoint to debit an account
    @PostMapping("/debit")
    public ResponseEntity<BankResponse> debitAccount(@RequestBody CreditDebitRequest debitRequest) {
        BankResponse response = accountService.debitAccount(debitRequest);
        return ResponseEntity.ok(response);
    }

    // Endpoint to transfer funds between accounts
    @PostMapping("/transfer")
    public ResponseEntity<BankResponse> transferFunds(@RequestParam String fromAccount,
                                                      @RequestParam String toAccount,
                                                      @RequestParam BigDecimal amount) {
        BankResponse response = accountService.transferFunds(fromAccount, toAccount, amount);
        return ResponseEntity.ok(response);
    }

    // Endpoint to get all accounts
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }

    // Endpoint to get loans by account number
    @GetMapping("/{accountNumber}/loans")
    public ResponseEntity<List<Loan>> getLoansByAccount(@PathVariable String accountNumber) {
        List<Loan> loans = accountService.getLoansByAccount(accountNumber);
        if (loans == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(loans);
    }
}
