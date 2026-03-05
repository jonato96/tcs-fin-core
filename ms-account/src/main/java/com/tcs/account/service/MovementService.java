package com.tcs.account.service;

import com.tcs.account.domain.Movement;
import com.tcs.account.dto.MovementRequestDto;
import com.tcs.account.dto.MovementResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface MovementService {
    MovementResponseDto create(MovementRequestDto dto);
    MovementResponseDto findById(Long id);
    List<MovementResponseDto> findByAccountNumber(String accountNumber);
    List<Movement> findByAccountAndDates(Long accountId, LocalDateTime start, LocalDateTime end);
}
