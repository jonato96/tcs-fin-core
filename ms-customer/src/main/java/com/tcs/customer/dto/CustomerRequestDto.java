package com.tcs.customer.dto;

import com.tcs.customer.domain.Gender;
import jakarta.validation.constraints.*;

public record CustomerRequestDto(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String name,

        @NotNull(message = "El género es obligatorio")
        Gender gender,

        @NotNull(message = "La edad es obligatoria")
        @Min(value = 18, message = "La edad mínima es 18")
        @Max(value = 110, message = "La edad máxima es 110")
        Integer age,

        @NotBlank(message = "La identificación es obligatoria")
        @Size(min = 1, max = 13, message = "La identificación debe tener entre 1 y 13 caracteres")
        @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "La identificación solo puede contener letras y números")
        String identification,

        @NotBlank(message = "La dirección es obligatoria")
        @Size(max = 200, message = "La dirección no puede superar los 200 caracteres")
        String address,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^[0-9]{10}$", message = "El teléfono debe tener exactamente 10 dígitos")
        String phone,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 10, message = "La contraseña debe tener entre 8 y 10 caracteres")
        String password
) {}
