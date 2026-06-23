package com.miguel.notificacao_api.controller;

import com.miguel.notificacao_api.business.AgendamentoService;
import com.miguel.notificacao_api.controller.dto.AgendamentoInDTO;
import com.miguel.notificacao_api.controller.dto.AgendamentoOutDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/agendamento")
public class AgendamentoController {

    private final AgendamentoService service;

    @PostMapping
    public ResponseEntity<AgendamentoOutDTO> gravarAgendamento(@RequestBody @Valid AgendamentoInDTO agendamento){
        return ResponseEntity.ok(service.gravarAgendamento(agendamento));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoOutDTO> buscarAgendamento(@PathVariable Long id){
        return ResponseEntity.ok(service.buscarAgendamento(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> cancelarAgendamento(@PathVariable Long id){
        service.cancelarAgendamento(id);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerAgendamento(@PathVariable Long id){
        service.cancelarAgendamento(id);
        return ResponseEntity.noContent().build();
    }
}
