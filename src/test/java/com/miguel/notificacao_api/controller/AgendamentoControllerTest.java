package com.miguel.notificacao_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miguel.notificacao_api.business.AgendamentoService;
import com.miguel.notificacao_api.controller.dto.AgendamentoInDTO;
import com.miguel.notificacao_api.controller.dto.AgendamentoOutDTO;
import com.miguel.notificacao_api.infra.enums.StatusNotificacao;
import com.miguel.notificacao_api.infra.exception.NotFoundException;
import jakarta.servlet.ServletException;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        LocalDateTime dataEnvio = LocalDateTime.of(2025, 2, 2, 2, 5, 5);
        inDTO = new AgendamentoInDTO("test@gmail.com", "238293232", "Teste Mensagem",
                dataEnvio);
        outDTO = new AgendamentoOutDTO(1L, "test@gmail.com", "238293232", "Teste Mensagem",
                dataEnvio, StatusNotificacao.AGENDADO);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void deveRetornaAgendamentoComDadosCorretos() throws Exception {
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

    @Test
    void deveBuscarAgendamentoERetorna200() throws Exception {
        when(service.buscarAgendamento(1L)).thenReturn(outDTO);
        mockMvc.perform(get("/agendamento/1").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(inDTO))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.emailDestinatario").value(outDTO.emailDestinatario()))
                .andExpect(jsonPath("$.telefoneDestinatario").value(outDTO.telefoneDestinatario()))
                .andExpect(jsonPath("$.mensagem").value(outDTO.mensagem()))
                .andExpect(jsonPath("$.dataEnvio").value("02-02-2025 02:05:05"))
                .andExpect(jsonPath("$.statusNotificacao").value(outDTO.statusNotificacao().toString()));
        verify(service, times(1)).buscarAgendamento(1L);
    }

    @Test
    void deveRetornarNotFoundExceptionQuandoNaoEncontrarAgendamento() {
        doThrow(new NotFoundException("id não encontrado")).when(service).buscarAgendamento(1L);

        ServletException exception = assertThrows(ServletException.class, () -> {
            mockMvc.perform(get("/agendamento/1")
                    .contentType(MediaType.APPLICATION_JSON));
        });

        assertInstanceOf(NotFoundException.class, exception.getCause());

        verify(service, times(1)).buscarAgendamento(1L);
    }


    @Test
    void deveCancelarAgendamentoERetorna200() throws Exception {
        doNothing().when(service).cancelarAgendamento(1L);
        mockMvc.perform(put("/agendamento/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(service, times(1)).cancelarAgendamento(1L);
    }

    @Test
    void deveRetornaNotFoundExceptionQuandoNaoEncontrarAgendamentoParaCancelar() {
        doThrow(new NotFoundException("id não encontrado")).when(service).cancelarAgendamento(1L);

        ServletException exception = assertThrows(ServletException.class, () -> mockMvc.perform(put("/agendamento/1").contentType(MediaType.APPLICATION_JSON)));

        assertInstanceOf(NotFoundException.class, exception.getCause());

        verify(service, times(1)).cancelarAgendamento(1L);
    }

}