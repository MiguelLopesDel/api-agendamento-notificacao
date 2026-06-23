package com.miguel.notificacao_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miguel.notificacao_api.business.AgendamentoService;
import com.miguel.notificacao_api.controller.dto.AgendamentoInDTO;
import com.miguel.notificacao_api.controller.dto.AgendamentoOutDTO;
import com.miguel.notificacao_api.infra.enums.CanalNotificacao;
import com.miguel.notificacao_api.infra.enums.StatusNotificacao;
import com.miguel.notificacao_api.infra.exception.GlobalExceptionHandler;
import com.miguel.notificacao_api.infra.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

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
        LocalDateTime dataEnvio = LocalDateTime.of(2030, 2, 2, 2, 5, 5);
        inDTO = new AgendamentoInDTO("test@gmail.com", null, null, "Teste Mensagem",
                CanalNotificacao.EMAIL, dataEnvio);
        outDTO = new AgendamentoOutDTO(1L, "test@gmail.com", null, null, "Teste Mensagem",
                CanalNotificacao.EMAIL, dataEnvio, StatusNotificacao.AGENDADO);
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
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
                .andExpect(jsonPath("$.destinatario").value(outDTO.destinatario()))
                .andExpect(jsonPath("$.mensagem").value(outDTO.mensagem()))
                .andExpect(jsonPath("$.canalNotificacao").value(outDTO.canalNotificacao().toString()))
                .andExpect(jsonPath("$.dataEnvio").value("02-02-2030 02:05:05"))
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
                .andExpect(jsonPath("$.destinatario").value(outDTO.destinatario()))
                .andExpect(jsonPath("$.mensagem").value(outDTO.mensagem()))
                .andExpect(jsonPath("$.canalNotificacao").value(outDTO.canalNotificacao().toString()))
                .andExpect(jsonPath("$.dataEnvio").value("02-02-2030 02:05:05"))
                .andExpect(jsonPath("$.statusNotificacao").value(outDTO.statusNotificacao().toString()));
        verify(service, times(1)).buscarAgendamento(1L);
    }

    @Test
    void deveRetornar404QuandoNaoEncontrarAgendamento() throws Exception {
        doThrow(new NotFoundException("id não encontrado")).when(service).buscarAgendamento(1L);

        mockMvc.perform(get("/agendamento/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("id não encontrado"));

        verify(service, times(1)).buscarAgendamento(1L);
    }


    @Test
    void deveCancelarAgendamentoViaPutERetorna200() throws Exception {
        doNothing().when(service).cancelarAgendamento(1L);
        mockMvc.perform(put("/agendamento/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
        verify(service, times(1)).cancelarAgendamento(1L);
    }

    @Test
    void deveRemoverAgendamentoViaDeleteERetorna204() throws Exception {
        doNothing().when(service).cancelarAgendamento(1L);
        mockMvc.perform(delete("/agendamento/1").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());
        verify(service, times(1)).cancelarAgendamento(1L);
    }

    @Test
    void deveRetorna404QuandoNaoEncontrarAgendamentoParaCancelar() throws Exception {
        doThrow(new NotFoundException("id não encontrado")).when(service).cancelarAgendamento(1L);

        mockMvc.perform(put("/agendamento/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("id não encontrado"));

        verify(service, times(1)).cancelarAgendamento(1L);
    }

    @Test
    void deveRetorna404QuandoNaoEncontrarAgendamentoParaRemover() throws Exception {
        doThrow(new NotFoundException("id não encontrado")).when(service).cancelarAgendamento(1L);

        mockMvc.perform(delete("/agendamento/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("id não encontrado"));

        verify(service, times(1)).cancelarAgendamento(1L);
    }

    @Test
    void deveRetornar400QuandoPayloadForInvalido() throws Exception {
        AgendamentoInDTO invalido = new AgendamentoInDTO("", null, null, "Teste Mensagem",
                CanalNotificacao.EMAIL, LocalDateTime.of(2030, 2, 2, 2, 5, 5));

        mockMvc.perform(post("/agendamento").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsBytes(invalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        verifyNoInteractions(service);
    }

}
