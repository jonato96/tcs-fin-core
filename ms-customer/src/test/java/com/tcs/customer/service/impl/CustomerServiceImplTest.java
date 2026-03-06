package com.tcs.customer.service.impl;

import com.tcs.customer.domain.Customer;
import com.tcs.customer.domain.Gender;
import com.tcs.customer.dto.CustomerRequestDto;
import com.tcs.customer.dto.CustomerResponseDto;
import com.tcs.customer.domain.exception.CustomerNotFoundException;
import com.tcs.customer.mapper.CustomerMapper;
import com.tcs.customer.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private CustomerMapper customerMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private CustomerServiceImpl customerService;

    @Test
    void create_whenIdentificationAlreadyExists_throwsException() {
        CustomerRequestDto dto = new CustomerRequestDto(
                "Jose Lema", Gender.MALE, 30, "1234567890",
                "Otavalo sn y principal", "098254785", "1234");

        when(customerRepository.existsByIdentification("1234567890")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> customerService.create(dto));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void create_success_encodesPasswordAndSetsActiveTrue() {
        CustomerRequestDto dto = new CustomerRequestDto(
                "Jose Lema", Gender.MALE, 30, "1234567890",
                "Otavalo sn y principal", "098254785", "1234");

        Customer entity = Customer.builder()
                .id(1L).name("Jose Lema").active(true).build();

        CustomerResponseDto expected = new CustomerResponseDto(
                1L, "Jose Lema", Gender.MALE, 30, "1234567890",
                "Otavalo sn y principal", "098254785", true);

        when(customerRepository.existsByIdentification("1234567890")).thenReturn(false);
        when(customerMapper.toEntity(dto)).thenReturn(entity);
        when(passwordEncoder.encode("1234")).thenReturn("$2a$encoded");
        when(customerRepository.save(entity)).thenReturn(entity);
        when(customerMapper.toDto(entity)).thenReturn(expected);

        CustomerResponseDto result = customerService.create(dto);

        assertNotNull(result);
        assertEquals("Jose Lema", result.name());
        assertTrue(result.active());
        verify(passwordEncoder).encode("1234");
    }

    @Test
    void findById_whenCustomerNotFound_throwsException() {
        when(customerRepository.findByIdAndActiveTrue(99L)).thenReturn(Optional.empty());

        assertThrows(CustomerNotFoundException.class, () -> customerService.findById(99L));
    }

    @Test
    void findById_success_returnsCustomer() {
        Customer customer = Customer.builder()
                .id(1L).name("Marianela Montalvo").active(true).build();

        CustomerResponseDto expected = new CustomerResponseDto(
                1L, "Marianela Montalvo", Gender.FEMALE, 25, "0987654321",
                "Amazonas y NNUU", "097548965", true);

        when(customerRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(expected);

        CustomerResponseDto result = customerService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Marianela Montalvo", result.name());
    }

    @Test
    void delete_whenCustomerNotFound_throwsException() {
        when(customerRepository.existsByIdAndActiveTrue(99L)).thenReturn(false);

        assertThrows(CustomerNotFoundException.class, () -> customerService.delete(99L));
        verify(customerRepository, never()).inactivateCustomer(anyLong());
    }

    @Test
    void delete_success_inactivatesCustomer() {
        when(customerRepository.existsByIdAndActiveTrue(1L)).thenReturn(true);

        customerService.delete(1L);

        verify(customerRepository).inactivateCustomer(1L);
    }
}
