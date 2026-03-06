package com.tcs.account.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.account.domain.AccountType;
import com.tcs.account.dto.AccountRequestDto;
import com.tcs.account.dto.AccountResponseDto;
import com.tcs.account.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private ConnectionFactory connectionFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAll_returnsEmptyList() throws Exception {
        when(accountService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findByAccountNumber_returnsAccount() throws Exception {
        AccountResponseDto response = new AccountResponseDto(
                1L, "478758", AccountType.SAVING,
                BigDecimal.valueOf(2000), BigDecimal.valueOf(1425),
                true, 1L, "Jose Lema");

        when(accountService.findByAccountNumber("478758")).thenReturn(response);

        mockMvc.perform(get("/accounts/478758"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accountNumber").value("478758"))
                .andExpect(jsonPath("$.customerName").value("Jose Lema"));
    }

    @Test
    void create_withInvalidBody_returns400() throws Exception {
        AccountRequestDto invalid = new AccountRequestDto("", null, BigDecimal.valueOf(-1), null, null);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }
}
