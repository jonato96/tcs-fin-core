package com.tcs.customer.dto;

import com.tcs.customer.domain.Gender;

public record CustomerResponseDto(
        Long id,
        String name,
        Gender gender,
        Integer age,
        String identification,
        String address,
        String phone,
        Boolean active
) {}
