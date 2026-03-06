package com.tcs.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tcs.customer.domain.Gender;
import com.tcs.customer.dto.CustomerRequestDto;
import com.tcs.customer.dto.CustomerResponseDto;
import com.tcs.customer.domain.exception.CustomerNotFoundException;
import com.tcs.customer.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerController.class)
class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private ConnectionFactory connectionFactory;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void findAll_returnsEmptyList() throws Exception {
        when(customerService.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void findById_returnsCustomer() throws Exception {
        CustomerResponseDto response = new CustomerResponseDto(
                1L, "Jose Lema", Gender.MALE, 30,
                "1234567890", "Otavalo sn y principal", "098254785", true);

        when(customerService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jose Lema"));
    }

    @Test
    void findById_whenNotFound_returns404() throws Exception {
        when(customerService.findById(99L)).thenThrow(new CustomerNotFoundException(99L));

        mockMvc.perform(get("/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_withValidBody_returns201() throws Exception {
        CustomerRequestDto request = new CustomerRequestDto(
                "Jose Lema", Gender.MALE, 30, "1234567890",
                "Otavalo sn y principal", "098254785", "1234");

        CustomerResponseDto response = new CustomerResponseDto(
                1L, "Jose Lema", Gender.MALE, 30,
                "1234567890", "Otavalo sn y principal", "098254785", true);

        when(customerService.create(any())).thenReturn(response);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Jose Lema"));
    }

    @Test
    void create_withInvalidBody_returns400() throws Exception {
        CustomerRequestDto invalid = new CustomerRequestDto(
                "", null, null, "", "", "", "");

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_whenNotFound_returns404() throws Exception {
        doThrow(new CustomerNotFoundException(99L)).when(customerService).delete(99L);

        mockMvc.perform(delete("/customers/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_success_returns204() throws Exception {
        mockMvc.perform(delete("/customers/1"))
                .andExpect(status().isNoContent());
    }
}
