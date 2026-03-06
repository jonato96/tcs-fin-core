package com.tcs.customer.mapper;

import com.tcs.customer.domain.Customer;
import com.tcs.customer.dto.CustomerRequestDto;
import com.tcs.customer.dto.CustomerResponseDto;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "password", ignore = true)
    Customer toEntity(CustomerRequestDto dto);

    CustomerResponseDto toDto(Customer customer);

    List<CustomerResponseDto> toListDto(List<Customer> customers);
}
