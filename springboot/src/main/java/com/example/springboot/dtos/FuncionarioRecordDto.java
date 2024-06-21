package com.example.springboot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record FuncionarioRecordDto(@NotBlank String nome, @NotBlank String email, @NotBlank String senha, @NotBlank String tipo, String cargo, Integer visitas) {

}
