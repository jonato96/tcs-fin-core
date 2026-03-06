package com.tcs.customer.repository;

import com.tcs.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByIdAndActiveTrue(Long id);

    boolean existsByIdAndActiveTrue(Long id);

    boolean existsByIdentification(String identification);

    @Modifying
    @Query("UPDATE Customer c SET c.active = false WHERE c.id = :id")
    void inactivateCustomer(@Param("id") Long id);
}
