package com.tcs.account.client;

import com.tcs.account.dto.client.CustomerClientResponseDto;

public interface CustomerClient {
    CustomerClientResponseDto findCustomer(Long customerId);
}
