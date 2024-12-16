package spring.example.bankService.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.example.bankService.Utils.AccountUtils;
import spring.example.bankService.dto.*;
import spring.example.bankService.entity.Account;
import spring.example.bankService.entity.Loan;
import spring.example.bankService.repository.AccountRepository;
import spring.example.bankService.repository.LoanRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private AccountRepository accountRepository;

    public BankResponse applyForLoan(LoanRequest loanRequest) {
        Account account = accountRepository.findByAccountNumber(loanRequest.getAccountNumber());

        if (account == null) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        // Initialize loan balance to loan amount
        Loan newLoan = Loan.builder()
                .account(account)
                .loanAmount(loanRequest.getLoanAmount())
                .loanBalance(loanRequest.getLoanAmount())
                .interestRate(loanRequest.getInterestRate())
                .startDate(LocalDate.now())
                .dueDate(loanRequest.getDueDate())
                .tenureMonths(loanRequest.getTenureMonths() != null ? loanRequest.getTenureMonths() : 12)
                .email(loanRequest.getEmail())
                .status("ACTIVE")
                .build();

        loanRepository.save(newLoan);

        // Update account balance (optional, if you want to deduct loan disbursement)
        account.setAccountBalance(account.getAccountBalance().add(newLoan.getLoanAmount()));
        accountRepository.save(account);

        LoanRequest loanInfo =  LoanInfo.builder()
                .loanAmount(newLoan.getLoanAmount())
                .loanBalance(newLoan.getLoanBalance())
                .interestRate(newLoan.getInterestRate())
                .accountNumber(newLoan.getAccount().getAccountNumber())
                .startDate(newLoan.getStartDate())
                .dueDate(newLoan.getDueDate())
                .tenureMonths(newLoan.getTenureMonths())
                .status(newLoan.getStatus())
                .build();

        return BankResponse.builder()
                .responseCode(AccountUtils.LOAN_CREATED_SUCCESS)
                .responseMessage("Loan successfully created.")
                .loanInfo(loanInfo)
                .build();
    }

    public List<LoanRequest> getLoansByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found!");
        }

        List<Loan> loans = loanRepository.findByAccount(account);
        return loans.stream()
                .map(loan -> LoanInfo.builder()
                        .loanAmount(loan.getLoanAmount())
                        .loanBalance(loan.getLoanBalance())
                        .interestRate(loan.getInterestRate())
                        .accountNumber(loan.getAccount().getAccountNumber())
                        .startDate(loan.getStartDate())
                        .dueDate(loan.getDueDate())
                        .status(loan.getStatus())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public BankResponse repayLoan(LoanRepaymentRequest repaymentRequest) {
        Loan loan = loanRepository.findById(repaymentRequest.getLoanId())
                .orElseThrow(() -> new IllegalArgumentException("Loan not found!"));

        BigDecimal repaymentAmount = repaymentRequest.getAmount();
        if (repaymentAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INVALID_AMOUNT_CODE)
                    .responseMessage("Invalid repayment amount.")
                    .build();
        }

        BigDecimal newLoanBalance = loan.getLoanBalance().subtract(repaymentAmount);

        if (newLoanBalance.compareTo(BigDecimal.ZERO) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INVALID_AMOUNT_CODE)
                    .responseMessage("Repayment exceeds loan balance.")
                    .build();
        }

        loan.setLoanBalance(newLoanBalance);
        if (newLoanBalance.equals(BigDecimal.ZERO)) {
            loan.setStatus("CLOSED");
        }

        loanRepository.save(loan);

        // Optionally update account balance
        Account account = loan.getAccount();
        account.setAccountBalance(account.getAccountBalance().subtract(repaymentAmount));
        accountRepository.save(account);

        LoanRequest updatedLoanInfo =LoanInfo.builder()
                .loanId(loan.getId())
                .loanAmount(loan.getLoanAmount())
                .loanBalance(loan.getLoanBalance())
                .interestRate(loan.getInterestRate())
                .accountNumber(loan.getAccount().getAccountNumber())
                .startDate(loan.getStartDate())
                .dueDate(loan.getDueDate())
                .status(loan.getStatus())
                .build();

        return BankResponse.builder()
                .responseCode(AccountUtils.LOAN_REPAYMENT_SUCCESS)
                .responseMessage("Loan repayment successful.")
                .loanInfo(updatedLoanInfo)
                .build();
    }
}
