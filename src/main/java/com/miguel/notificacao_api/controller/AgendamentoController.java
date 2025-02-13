package com.miguel.notificacao_api.controller;

import com.miguel.notificacao_api.business.AgendamentoService;
import com.miguel.notificacao_api.controller.dto.AgendamentoInDTO;
import com.miguel.notificacao_api.controller.dto.AgendamentoOutDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agendamento")
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping
    public ResponseEntity<AgendamentoOutDTO> gravarAgendamento(@RequestBody AgendamentoInDTO agendamento){
        return ResponseEntity.ok(service.gravarAgendamento(agendamento));
    }
}
