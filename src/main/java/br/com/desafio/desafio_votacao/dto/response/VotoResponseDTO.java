package br.com.desafio.desafio_votacao.dto.response;

import br.com.desafio.desafio_votacao.enums.TipoVoto;
import lombok.Data;

@Data
public class VotoResponseDTO {

  private Long idVoto;

  private Long idUsuario;

  private Long idSessaoVotacao;

  private TipoVoto tipoVoto;

}
