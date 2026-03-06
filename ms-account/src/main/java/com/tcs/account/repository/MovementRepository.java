package com.tcs.account.repository;

import com.tcs.account.domain.Movement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovementRepository extends JpaRepository<Movement, Long> {

    List<Movement> findByAccount_Id(Long accountId);

    List<Movement> findByAccount_IdAndDateBetween(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
}
