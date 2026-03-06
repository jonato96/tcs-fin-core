package com.tcs.account.mapper;

import com.tcs.account.domain.Movement;
import com.tcs.account.dto.MovementResponseDto;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MovementMapper {

    @Mapping(target = "accountNumber", source = "account.accountNumber")
    MovementResponseDto toResponseDto(Movement movement);
}
