package com.miguel.notificacao_api.business;

import com.miguel.notificacao_api.controller.dto.AgendamentoInDTO;
import com.miguel.notificacao_api.controller.dto.AgendamentoOutDTO;
import com.miguel.notificacao_api.infra.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

    private final AgendamentoRepository repository;
    private final IAgendamentoMapper agendamentoMapper;

    public AgendamentoOutDTO gravarAgendamento(AgendamentoInDTO inDTO) {
        return agendamentoMapper.toEntity(repository.save(agendamentoMapper.toEntity(inDTO)));
    }

}
