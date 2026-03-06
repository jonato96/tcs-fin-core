package com.tcs.account.dto.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomerClientResponseDto(Long id, String name, Boolean active) {}
