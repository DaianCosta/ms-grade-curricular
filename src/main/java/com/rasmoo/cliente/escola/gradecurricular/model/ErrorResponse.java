package com.rasmoo.cliente.escola.gradecurricular.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ErrorResponse {
    private final String mensagem;
    private final int httpStatus;
    private final long timeStamp;
}
