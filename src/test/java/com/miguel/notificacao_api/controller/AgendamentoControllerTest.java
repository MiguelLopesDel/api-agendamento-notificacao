package com.miguel.notificacao_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miguel.notificacao_api.business.AgendamentoService;
import com.miguel.notificacao_api.controller.dto.AgendamentoInDTO;
import com.miguel.notificacao_api.controller.dto.AgendamentoOutDTO;
import com.miguel.notificacao_api.infra.enums.StatusNotificacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AgendamentoControllerTest {

    @InjectMocks
    AgendamentoController controller;
    @Mock
    AgendamentoService service;

    private MockMvc mockMvc;
    private final ObjectMapper mapper = new ObjectMapper();

    private AgendamentoInDTO inDTO;
    private AgendamentoOutDTO outDTO;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.of(2025, 2, 2,2,5,5);
        inDTO = new AgendamentoInDTO("test@gmail.com", "238293232", "Teste Mensagem",
                now);
        outDTO = new AgendamentoOutDTO(1L, "test@gmail.com", "238293232", "Teste Mensagem",
                now, StatusNotificacao.AGENDADO);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void criaAgendamentoComSucesso() throws Exception {
        when(service.gravarAgendamento(inDTO)).thenReturn(outDTO);
        mockMvc.perform(post("/agendamento").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(inDTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.emailDestinatario").value(outDTO.emailDestinatario()))
                .andExpect(jsonPath("$.telefoneDestinatario").value(outDTO.telefoneDestinatario()))
                .andExpect(jsonPath("$.mensagem").value(outDTO.mensagem()))
                .andExpect(jsonPath("$.dataEnvio").value("02-02-2025 02:05:05"))
                .andExpect(jsonPath("$.statusNotificacao").value(outDTO.statusNotificacao().toString()));
        verify(service, times(1)).gravarAgendamento(inDTO);
    }
}