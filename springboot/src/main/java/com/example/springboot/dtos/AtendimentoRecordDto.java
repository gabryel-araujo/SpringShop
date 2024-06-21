package com.example.springboot.dtos;

import jakarta.validation.constraints.NotBlank;

public record AtendimentoRecordDto (@NotBlank String checkin, @NotBlank String id_funcionario, @NotBlank String id_cliente, @NotBlank String data_atendimento, @NotBlank String link_relatorio ){
}
