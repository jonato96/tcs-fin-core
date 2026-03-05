package com.tcs.customer.service.impl;

import com.tcs.customer.domain.Customer;
import com.tcs.customer.dto.CustomerRequestDto;
import com.tcs.customer.dto.CustomerResponseDto;
import com.tcs.customer.exception.CustomerNotFoundException;
import com.tcs.customer.mapper.CustomerMapper;
import com.tcs.customer.repository.CustomerRepository;
import com.tcs.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public CustomerResponseDto create(CustomerRequestDto dto) {
        if (customerRepository.existsByIdentification(dto.identification())) {
            throw new IllegalArgumentException("Ya existe un cliente con la identificación: " + dto.identification());
        }
        Customer customer = customerMapper.toEntity(dto);
        customer.setPassword(passwordEncoder.encode(dto.password()));
        customer.setActive(true);
        return customerMapper.toDto(customerRepository.save(customer));
    }

    @Override
    @Transactional
    public CustomerResponseDto update(Long id, CustomerRequestDto dto) {
        Customer existing = getActiveCustomerOrThrow(id);
        Customer updated = customerMapper.toEntity(dto);
        updated.setId(existing.getId());
        updated.setActive(existing.getActive());
        updated.setPassword(passwordEncoder.encode(dto.password()));
        return customerMapper.toDto(customerRepository.save(updated));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!customerRepository.existsByIdAndActiveTrue(id)) {
            throw new CustomerNotFoundException(id);
        }
        customerRepository.inactivateCustomer(id);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponseDto findById(Long id) {
        return customerMapper.toDto(getActiveCustomerOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponseDto> findAll() {
        return customerMapper.toListDto(customerRepository.findAll());
    }

    private Customer getActiveCustomerOrThrow(Long id) {
        return customerRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
    }
}
