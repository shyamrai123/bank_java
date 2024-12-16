package spring.example.bankService.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.example.bankService.entity.Account;
import spring.example.bankService.entity.Loan;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan>findByAccount(Account account);
}
