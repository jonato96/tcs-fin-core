package com.tcs.account.controller;

import com.tcs.account.dto.MovementRequestDto;
import com.tcs.account.dto.MovementResponseDto;
import com.tcs.account.service.MovementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movements")
@RequiredArgsConstructor
public class MovementController {

    private final MovementService movementService;

    @PostMapping
    public ResponseEntity<MovementResponseDto> create(@Valid @RequestBody MovementRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(movementService.create(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovementResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(movementService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<MovementResponseDto>> findByAccountNumber(
            @RequestParam String accountNumber) {
        return ResponseEntity.ok(movementService.findByAccountNumber(accountNumber));
    }
}
