package com.tcs.customer.service;

import com.tcs.customer.dto.CustomerRequestDto;
import com.tcs.customer.dto.CustomerResponseDto;

import java.util.List;

public interface CustomerService {
    CustomerResponseDto create(CustomerRequestDto dto);
    CustomerResponseDto update(Long id, CustomerRequestDto dto);
    void delete(Long id);
    CustomerResponseDto findById(Long id);
    List<CustomerResponseDto> findAll();
}
