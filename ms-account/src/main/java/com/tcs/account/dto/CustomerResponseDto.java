package com.tcs.account.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomerResponseDto(Long id, String name, Boolean active) {}
