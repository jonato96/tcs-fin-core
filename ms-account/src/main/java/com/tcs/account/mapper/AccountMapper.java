package com.tcs.account.mapper;

import com.tcs.account.domain.Account;
import com.tcs.account.dto.AccountRequestDto;
import com.tcs.account.dto.AccountResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    @Mapping(target = "customerName", source = "customerName")
    AccountResponseDto toResponseDto(Account account, String customerName);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "availableBalance", source = "initialBalance")
    Account toEntity(AccountRequestDto dto);
}
