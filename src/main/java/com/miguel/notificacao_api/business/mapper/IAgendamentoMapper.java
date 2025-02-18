package com.miguel.notificacao_api.business.mapper;

import com.miguel.notificacao_api.controller.dto.AgendamentoInDTO;
import com.miguel.notificacao_api.controller.dto.AgendamentoOutDTO;
import com.miguel.notificacao_api.infra.entity.Agendamento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel = SPRING)
public interface IAgendamentoMapper {

    Agendamento toEntity(AgendamentoInDTO inDTO);
    AgendamentoOutDTO toOut(Agendamento agendamento);
    @Mapping(target = "dataModificacao", expression = "java(LocalDateTime.now())")
    @Mapping(target = "statusNotificacao", expression = "java(StatusNotificacao.CANCELADO)")
    Agendamento toEntityCancelado(Agendamento agendamento);
}
