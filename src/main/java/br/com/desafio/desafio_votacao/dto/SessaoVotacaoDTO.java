package br.com.desafio.desafio_votacao.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SessaoVotacaoDTO {

  @NotNull
  private Long idPauta;

  @NotNull
  private LocalDateTime inicio;

  @NotNull
  private LocalDateTime fim;

  @NotNull
  private Integer duracaoSessaoMinutos;

}
