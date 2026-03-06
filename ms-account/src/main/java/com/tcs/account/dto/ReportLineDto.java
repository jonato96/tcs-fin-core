package com.tcs.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReportLineDto(
        @JsonProperty("Fecha")
        @JsonFormat(pattern = "d/M/yyyy HH:mm:ss")
        LocalDateTime date,

        @JsonProperty("Cliente")
        String customerName,

        @JsonProperty("Numero Cuenta")
        String accountNumber,

        @JsonProperty("Tipo")
        String accountType,

        @JsonProperty("Saldo Inicial")
        BigDecimal initialBalance,

        @JsonProperty("Estado")
        Boolean active,

        @JsonProperty("Movimiento")
        BigDecimal movement,

        @JsonProperty("Saldo Disponible")
        BigDecimal availableBalance
) {}
