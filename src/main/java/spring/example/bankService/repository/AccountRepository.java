package spring.example.bankService.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import spring.example.bankService.entity.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Boolean existsByEmail(String email);
    Boolean existsByAccountNumber(String accountNumber);
    Account findByAccountNumber(String accountNumber);
}
