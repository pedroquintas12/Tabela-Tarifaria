package com.br.desafio.ras.core.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerController {

    private final String logger = this.getClass().getName();

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleGenericException(Exception ex) {
        log.error("Erro interno não tratado", ex);
        ErroResponse erro = new ErroResponse(
                500,
                "Erro interno ao processar a requisição",
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(erro);
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErroResponse> handleRegraNegocio(
            BusinessRuleException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<ErroResponse> handleNotFound(
            RecursoNaoEncontradoException ex) {

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(
                        HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                ));
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleJsonParseError(
            HttpMessageNotReadableException ex) {

        return ResponseEntity.badRequest().body(
                new ErroResponse(
                        400,
                        "JSON inválido ou mal formatado",
                        LocalDateTime.now()
                )
        );
    }
}