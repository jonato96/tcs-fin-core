package com.tcs.account.service.impl;

import com.tcs.account.domain.Account;
import com.tcs.account.domain.Movement;
import com.tcs.account.domain.MovementType;
import com.tcs.account.domain.exception.InsufficientBalanceException;
import com.tcs.account.dto.MovementRequestDto;
import com.tcs.account.dto.MovementResponseDto;
import com.tcs.account.mapper.MovementMapper;
import com.tcs.account.repository.MovementRepository;
import com.tcs.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MovementServiceImplTest {

    @Mock
    private MovementRepository movementRepository;
    @Mock
    private MovementMapper movementMapper;
    @Mock
    private AccountService accountService;
    @InjectMocks
    private MovementServiceImpl movementService;

    @Test
    void create_withInsufficientBalance_throwsException() {
        Account account = Account.builder()
                .id(1L).accountNumber("478758")
                .availableBalance(BigDecimal.valueOf(100))
                .active(true).build();

        MovementRequestDto dto = new MovementRequestDto("478758", BigDecimal.valueOf(200), MovementType.DEBIT);

        when(accountService.getActiveAccount("478758")).thenReturn(account);

        assertThrows(InsufficientBalanceException.class, () -> movementService.create(dto));
        verify(movementRepository, never()).save(any());
    }

    @Test
    void create_creditMovement_updatesBalanceCorrectly() {
        Account account = Account.builder()
                .id(1L).accountNumber("225487")
                .availableBalance(BigDecimal.valueOf(100))
                .active(true).build();

        MovementRequestDto dto = new MovementRequestDto("225487", BigDecimal.valueOf(600), MovementType.CREDIT);

        Movement saved = Movement.builder()
                .id(1L).date(LocalDateTime.now()).movementType(MovementType.CREDIT)
                .amount(BigDecimal.valueOf(600))
                .balanceBefore(BigDecimal.valueOf(100))
                .balanceAfter(BigDecimal.valueOf(700))
                .account(account).build();

        MovementResponseDto expected = new MovementResponseDto(
                1L, LocalDateTime.now(), MovementType.CREDIT,
                BigDecimal.valueOf(600), BigDecimal.valueOf(100), BigDecimal.valueOf(700), "225487");

        when(accountService.getActiveAccount("225487")).thenReturn(account);
        when(movementRepository.save(any())).thenReturn(saved);
        when(movementMapper.toResponseDto(saved)).thenReturn(expected);

        MovementResponseDto result = movementService.create(dto);

        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(700), result.balanceAfter());
        verify(accountService).updateBalance(BigDecimal.valueOf(700), 1L);
    }
}
