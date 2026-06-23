package com.miguel.notificacao_api.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.miguel.notificacao_api.infra.enums.CanalNotificacao;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record AgendamentoInDTO(@NotBlank String destinatario,
                               String emailDestinatario,
                               String telefoneDestinatario,
                               @NotBlank String mensagem,
                               @NotNull CanalNotificacao canalNotificacao,
                               @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
                               @NotNull @Future LocalDateTime dataEnvio) {
}
