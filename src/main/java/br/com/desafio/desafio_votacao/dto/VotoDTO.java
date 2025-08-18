package br.com.desafio.desafio_votacao.dto;

import br.com.desafio.desafio_votacao.enums.TipoVoto;
import lombok.Data;

@Data
public class VotoDTO {

  private PautaDTO pautaDTO;

  private Long idUsuario;

  private TipoVoto tipoVoto;


}
