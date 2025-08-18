package br.com.desafio.desafio_votacao.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneric(Exception ex) {

    log.error("Erro inesperado: {}", ex.getMessage(), ex);

    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body("Erro interno do servidor. Tente novamente mais tarde.");
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<String> handleBusiness(BusinessException ex) {

    log.error("Erro de negócio: {}", ex.getMessage(), ex);

    return ResponseEntity.status(BAD_REQUEST)
        .body(ex.getMessage());
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {

    log.error("Entidade não encontrada: {}", ex.getMessage(), ex);

    return ResponseEntity.status(NOT_FOUND)
        .body(ex.getMessage());
  }
}
