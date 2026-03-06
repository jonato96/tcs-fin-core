package com.tcs.customer.messaging;

import com.tcs.customer.dto.CustomerResponseDto;

public interface CustomerQueryHandler {
    CustomerResponseDto findCustomer(Long customerId);
}
