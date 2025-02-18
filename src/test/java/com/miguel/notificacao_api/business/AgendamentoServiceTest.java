package com.miguel.notificacao_api.business;

import com.miguel.notificacao_api.business.mapper.IAgendamentoMapper;
import com.miguel.notificacao_api.controller.dto.AgendamentoInDTO;
import com.miguel.notificacao_api.controller.dto.AgendamentoOutDTO;
import com.miguel.notificacao_api.infra.entity.Agendamento;
import com.miguel.notificacao_api.infra.enums.StatusNotificacao;
import com.miguel.notificacao_api.infra.repository.AgendamentoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTest {

    @InjectMocks
    private AgendamentoService service;
    @Mock
    private AgendamentoRepository repository;
    @Mock
    private IAgendamentoMapper mapper;

    private AgendamentoInDTO inDTO;
    private AgendamentoOutDTO outDTO;
    private Agendamento agendamento;

    @BeforeEach
    void setUp() {
        LocalDateTime envio = LocalDateTime.of(2025, 2, 2, 2, 5, 5);
        agendamento = new Agendamento(1L, "test@gmail.com", "238293232", envio, LocalDateTime.now(), null, "Teste Mensagem", StatusNotificacao.AGENDADO);
        inDTO = new AgendamentoInDTO("test@gmail.com", "238293232", "Teste Mensagem",
                envio);
        outDTO = new AgendamentoOutDTO(1L, "test@gmail.com", "238293232", "Teste Mensagem",
                envio, StatusNotificacao.AGENDADO);
    }

    @Test
    void criaAgendamento() {
        when(mapper.toEntity(inDTO)).thenReturn(agendamento);
        when(repository.save(agendamento)).thenReturn(agendamento);
        when(mapper.toOut(agendamento)).thenReturn(outDTO);

        AgendamentoOutDTO out = service.gravarAgendamento(inDTO);
        verify(mapper, times(1)).toEntity(inDTO);
        verify(repository, times(1)).save(agendamento);
        verify(mapper, times(1)).toOut(agendamento);
        assertThat(out).usingRecursiveComparison().isEqualTo(outDTO);
    }

    @Test
    void buscarAgendamento() {
    }

    @Test
    void cancelarAgendamento() {
    }
}