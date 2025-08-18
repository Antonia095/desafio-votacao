package br.com.desafio.desafio_votacao.dto;

import br.com.desafio.desafio_votacao.enums.StatusSessaoVotacao;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SessaoVotacaoDTO {

  private Long idPauta;

  private LocalDateTime inicio;

  private LocalDateTime fim;

  private StatusSessaoVotacao statusSessaoVotacao;

}
