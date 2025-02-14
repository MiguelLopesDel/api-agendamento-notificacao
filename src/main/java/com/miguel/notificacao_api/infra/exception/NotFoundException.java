package com.miguel.notificacao_api.infra.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String idNãoEncontrado) {
        super(idNãoEncontrado);
    }

}
