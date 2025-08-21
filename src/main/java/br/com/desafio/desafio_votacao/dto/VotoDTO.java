package br.com.desafio.desafio_votacao.dto;

import br.com.desafio.desafio_votacao.enums.TipoVoto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VotoDTO {

  @NotNull
  private Long idUsuario;

  @NotNull
  private Long idSessaoVotacao;

  @NotNull
  private TipoVoto tipoVoto;

}
