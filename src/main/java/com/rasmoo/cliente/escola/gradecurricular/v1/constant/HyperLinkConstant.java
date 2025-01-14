package com.rasmoo.cliente.escola.gradecurricular.v1.constant;

import lombok.Getter;

@Getter
public enum HyperLinkConstant {

    ATUALIZAR("UPDATE"),
    EXCLUIR("DELETE"),
    LISTAR("GET_ALL"),
    CONSULTAR("GET");

    private final String valor;

    private HyperLinkConstant(String valor) {
        this.valor = valor;
    }

}