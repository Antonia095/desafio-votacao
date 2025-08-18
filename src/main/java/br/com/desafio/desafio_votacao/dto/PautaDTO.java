package br.com.desafio.desafio_votacao.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PautaDTO {

  @NotBlank
  private String titulo;

  @NotBlank
  private String descricao;

}
