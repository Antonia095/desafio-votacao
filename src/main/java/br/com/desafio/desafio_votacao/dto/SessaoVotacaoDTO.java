package br.com.desafio.desafio_votacao.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SessaoVotacaoDTO {

  @NotNull
  private Long idPauta;

  private LocalDateTime inicio;

  private LocalDateTime fim;

}
