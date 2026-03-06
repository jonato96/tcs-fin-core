package com.tcs.account.service.impl;

import com.tcs.account.domain.Account;
import com.tcs.account.domain.Movement;
import com.tcs.account.domain.MovementType;
import com.tcs.account.domain.exception.InsufficientBalanceException;
import com.tcs.account.domain.exception.MovementNotFoundException;
import com.tcs.account.dto.MovementRequestDto;
import com.tcs.account.dto.MovementResponseDto;
import com.tcs.account.mapper.MovementMapper;
import com.tcs.account.repository.MovementRepository;
import com.tcs.account.service.AccountService;
import com.tcs.account.service.MovementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MovementServiceImpl implements MovementService {

    private final MovementRepository movementRepository;
    private final MovementMapper movementMapper;
    private final AccountService accountService;

    @Override
    @Transactional
    public MovementResponseDto create(MovementRequestDto dto) {
        Account account = accountService.getActiveAccount(dto.accountNumber());

        BigDecimal value = dto.movementType() == MovementType.DEBIT
                ? dto.value().negate()
                : dto.value();

        BigDecimal balanceBefore = account.getAvailableBalance();
        BigDecimal balanceAfter = balanceBefore.add(value);

        if (balanceAfter.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException();
        }

        Movement movement = Movement.builder()
                .date(LocalDateTime.now())
                .movementType(dto.movementType())
                .amount(value)
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .account(account)
                .build();

        accountService.updateBalance(balanceAfter, account.getId());
        return movementMapper.toResponseDto(movementRepository.save(movement));
    }

    @Override
    @Transactional(readOnly = true)
    public MovementResponseDto findById(Long id) {
        return movementMapper.toResponseDto(
                movementRepository.findById(id)
                        .orElseThrow(() -> new MovementNotFoundException(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<MovementResponseDto> findByAccountNumber(String accountNumber) {
        Account account = accountService.getActiveAccount(accountNumber);
        return movementRepository.findByAccount_Id(account.getId()).stream()
                .map(movementMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Movement> findByAccountAndDates(Long accountId, LocalDateTime start, LocalDateTime end) {
        return movementRepository.findByAccount_IdAndDateBetween(accountId, start, end);
    }
}
