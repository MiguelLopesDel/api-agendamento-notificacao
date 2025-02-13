package com.miguel.notificacao_api.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record AgendamentoInDTO(String emailDestinatario, String telefoneDestinatario, String mensagem,
                               @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
                             LocalDateTime dataEnvio) {
}
