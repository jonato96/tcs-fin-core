package com.tcs.account.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReportLineDto(
        @JsonProperty("Fecha")
        @JsonFormat(pattern = "d/M/yyyy HH:mm:ss")
        LocalDateTime fecha,

        @JsonProperty("Cliente")
        String cliente,

        @JsonProperty("Numero Cuenta")
        String numeroCuenta,

        @JsonProperty("Tipo")
        String tipo,

        @JsonProperty("Saldo Inicial")
        BigDecimal saldoInicial,

        @JsonProperty("Estado")
        Boolean estado,

        @JsonProperty("Movimiento")
        BigDecimal movimiento,

        @JsonProperty("Saldo Disponible")
        BigDecimal saldoDisponible
) {}
