package com.example.springboot.dtos;

import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record ClienteRecordDto (@NotBlank String nome, @NotBlank String rua, String numero, String complemento, @NotBlank String bairro, String telefone, Date data_cadastro){
}
