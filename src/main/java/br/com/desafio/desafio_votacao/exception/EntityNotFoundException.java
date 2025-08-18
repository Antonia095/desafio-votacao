package br.com.desafio.desafio_votacao.exception;

public class EntityNotFoundException extends RuntimeException{

  public EntityNotFoundException(String message) {
    super(message);
  }
}
