package com.tcs.account.repository;

import com.tcs.account.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByAccountNumber(String accountNumber);

    List<Account> findByCustomerId(Long customerId);

    boolean existsByAccountNumber(String accountNumber);

    @Modifying
    @Query("UPDATE Account a SET a.active = false WHERE a.id = :id")
    void inactivateAccount(@Param("id") Long id);

    @Modifying
    @Query("UPDATE Account a SET a.availableBalance = :balance WHERE a.id = :id")
    void updateBalance(@Param("balance") BigDecimal balance, @Param("id") Long id);
}
