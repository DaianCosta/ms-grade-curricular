package com.rasmoo.cliente.escola.gradecurricular.v1.handler;

import java.util.HashMap;
import java.util.Map;

import com.rasmoo.cliente.escola.gradecurricular.v1.exception.CursoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.rasmoo.cliente.escola.gradecurricular.v1.exception.MateriaException;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.ErrorMapResponse;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.ErrorMapResponse.ErrorMapResponseBuilder;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.ErrorResponse;
import com.rasmoo.cliente.escola.gradecurricular.v1.model.ErrorResponse.ErrorResponseBuilder;

@ControllerAdvice
public class ResourceHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMapResponse> handlerMethodArgumentNotValidException(MethodArgumentNotValidException m){

        Map<String,String> erros = new HashMap<>();
        m.getBindingResult().getAllErrors().forEach(erro->{
            String campo = ((FieldError)erro).getField();
            String mensagem = erro.getDefaultMessage();
            erros.put(campo,mensagem);
        });
        ErrorMapResponseBuilder errorMap = ErrorMapResponse.builder();
        errorMap.erros(erros)
                .httpStatus(HttpStatus.BAD_REQUEST.value())
                .timeStamp(System.currentTimeMillis());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMap.build());

    }

    @ExceptionHandler(MateriaException.class)
    public ResponseEntity<ErrorResponse> handlerMateriaException(MateriaException m){
        ErrorResponseBuilder erro = ErrorResponse.builder();
        erro.httpStatus(m.getHttpStatus().value());
        erro.mensagem(m.getMessage());
        erro.timeStamp(System.currentTimeMillis());
        return ResponseEntity.status(m.getHttpStatus()).body(erro.build());

    }

    @ExceptionHandler(CursoException.class)
    public ResponseEntity<ErrorResponse> handlerCursoException(MateriaException m){
        ErrorResponseBuilder erro = ErrorResponse.builder();
        erro.httpStatus(m.getHttpStatus().value());
        erro.mensagem(m.getMessage());
        erro.timeStamp(System.currentTimeMillis());
        return ResponseEntity.status(m.getHttpStatus()).body(erro.build());

    }
}
