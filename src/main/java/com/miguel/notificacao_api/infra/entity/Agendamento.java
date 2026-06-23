package com.miguel.notificacao_api.infra.entity;

import com.miguel.notificacao_api.infra.enums.StatusNotificacao;
import com.miguel.notificacao_api.infra.enums.CanalNotificacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "agendamento")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String destinatario;
    private String emailDestinatario;
    private String telefoneDestinatario;
    private LocalDateTime dataEnvio;
    private LocalDateTime dataAgendamento;
    private LocalDateTime dataModificacao;
    private String mensagem;
    @Enumerated(EnumType.STRING)
    private CanalNotificacao canalNotificacao;
    @Enumerated(EnumType.STRING)
    private StatusNotificacao statusNotificacao;

    @PrePersist
    private void prePersist() {
        dataAgendamento = LocalDateTime.now();
        statusNotificacao = StatusNotificacao.AGENDADO;
    }
}
