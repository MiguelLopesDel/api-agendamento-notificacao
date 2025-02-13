package com.miguel.notificacao_api.infra.repository;

import com.miguel.notificacao_api.infra.entity.Agendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
}
