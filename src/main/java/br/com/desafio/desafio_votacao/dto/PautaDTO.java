package br.com.desafio.desafio_votacao.dto;

import br.com.desafio.desafio_votacao.enums.StatusPauta;
import lombok.Data;

@Data
public class PautaDTO {

  private String titulo;

  private String descricao;

  private int quantidadeVotos;

  private StatusPauta statusPauta;

}
