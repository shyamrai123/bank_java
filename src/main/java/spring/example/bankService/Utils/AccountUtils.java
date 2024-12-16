package spring.example.bankService.Utils;

import java.time.Year;

public class AccountUtils {

    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This user already has an account Created!";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";
    public static final String ACCOUNT_NOT_EXIST_CODE = "003";
    public static final String ACCOUNT_NOT_EXIST_MESSAGE = "User with the provided Account Number does not exist";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_SUCCESS = "User Account Found";
    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_SUCCESS_MESSAGE = "User Account was credited successfully";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance";
    public static final String ACCOUNT_DEBITED_SUCCESS = "007";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Account has been successfully debited";
    public  static final String INVALID_AMOUNT_CODE = "00";
    public  static final String INVALID_AMOUNT_MESSAGE = "Amount must be greater than zero.";
    public  static final String TRANSACTION_SUCCESS_CODE = "008";
    public  static final String TRANSACTION_SUCCESS_MESSAGE = "Transaction completed successfully.";
    public  static final String TRANSACTION_SENDER_AMOUNT   =" the available balance of sender is ";
    public static final String LOAN_CREATED_SUCCESS = "200";
    public static final String LOAN_CREATION_MESSAGE = "Loan Account has been successfully created!";
    public static final String LOAN_REPAYMENT_SUCCESS = "200";
    public  static final String LOAN_SUCCESS_MESSAGE = "Loan completed successfully.";

    public static final String LOAN_REPAYMENT_FAILED = "400";




    public static String generateAccountNumber(){

        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        //generate a random number between min and max
        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);
        //convert the current and randomNumber to strings, then concatenate them

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();

        return accountNumber.append(year).append(randomNumber).toString();


    }
}
