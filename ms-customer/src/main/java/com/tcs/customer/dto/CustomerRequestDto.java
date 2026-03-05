package com.tcs.customer.dto;

import com.tcs.customer.domain.Gender;
import jakarta.validation.constraints.*;

public record CustomerRequestDto(
        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotNull(message = "El género es obligatorio")
        Gender gender,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 18, message = "La edad mínima es 18")
        @Max(value = 110, message = "La edad máxima es 110")
        Integer age,

        @NotBlank(message = "La identificación es obligatoria")
        @Size(min = 1, max = 13, message = "La identificación debe tener entre 1 y 13 caracteres")
        String identification,

        @NotBlank(message = "La dirección es obligatoria")
        String address,

        @NotBlank(message = "El teléfono es obligatorio")
        String phone,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {}
