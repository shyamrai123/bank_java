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
import java.util.List;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private EmailService emailService;

    public BankResponse createAccount(AccountRequest accountRequest) {
        if (accountRepository.existsByEmail(accountRequest.getEmail())) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }

        Account newAccount = Account.builder()
                .firstName(accountRequest.getFirstName())
                .lastName(accountRequest.getLastName())
                .otherName(accountRequest.getOtherName())
                .gender(accountRequest.getGender())
                .address(accountRequest.getAddress())
                .stateOfOrigin(accountRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .accountBalance(BigDecimal.ZERO)
                .email(accountRequest.getEmail())
                .phoneNumber(accountRequest.getPhoneNumber())
                .alternativePhoneNumber(accountRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .build();

        Account savedAccount = accountRepository.save(newAccount);

        // Send notification email
        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedAccount.getEmail())
                .subject("Account Creation Successful")
                .messageBody("Congratulations! Your account has been created.\n" +
                        "Account Number: " + savedAccount.getAccountNumber())
                .build();
        emailService.sendEmailAlert(emailDetails);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedAccount.getAccountBalance())
                        .accountNumber(savedAccount.getAccountNumber())
                        .accountName(savedAccount.getFirstName() + " " + savedAccount.getLastName())
                        .build())
                .build();
    }

    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        Account account = accountRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());

        if (account == null) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        account.setAccountBalance(account.getAccountBalance().add(creditDebitRequest.getAmount()));
        Account updatedAccount = accountRepository.save(account);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_SUCCESS_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(updatedAccount.getFirstName() + " " + updatedAccount.getLastName())
                        .accountNumber(updatedAccount.getAccountNumber())
                        .accountBalance(updatedAccount.getAccountBalance())
                        .build())
                .build();
    }

    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {
        Account account = accountRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());

        if (account == null) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.ACCOUNT_NOT_EXIST_CODE)
                    .responseMessage(AccountUtils.ACCOUNT_NOT_EXIST_MESSAGE)
                    .build();
        }

        if (creditDebitRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0 ||
                account.getAccountBalance().compareTo(creditDebitRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INVALID_AMOUNT_CODE)
                    .responseMessage("Insufficient balance or invalid amount.")
                    .build();
        }

        account.setAccountBalance(account.getAccountBalance().subtract(creditDebitRequest.getAmount()));
        Account updatedAccount = accountRepository.save(account);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_DEBITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountName(updatedAccount.getFirstName() + " " + updatedAccount.getLastName())
                        .accountNumber(updatedAccount.getAccountNumber())
                        .accountBalance(updatedAccount.getAccountBalance())
                        .build())
                .build();
    }

    @Transactional
    public BankResponse transferFunds(String fromAccount, String toAccount, BigDecimal amount) {
        Account sender = accountRepository.findByAccountNumber(fromAccount);
        Account receiver = accountRepository.findByAccountNumber(toAccount);

        if (sender == null || receiver == null || sender.getAccountBalance().compareTo(amount) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage("Transaction failed due to insufficient funds or invalid accounts.")
                    .build();
        }

        sender.setAccountBalance(sender.getAccountBalance().subtract(amount));
        receiver.setAccountBalance(receiver.getAccountBalance().add(amount));

        accountRepository.save(sender);
        accountRepository.save(receiver);

        return BankResponse.builder()
                .responseCode(AccountUtils.TRANSACTION_SUCCESS_CODE)
                .responseMessage("Transaction successful.")
                .build();
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Loan> getLoansByAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        return account != null? account.getLoans(): null;
    }
}
