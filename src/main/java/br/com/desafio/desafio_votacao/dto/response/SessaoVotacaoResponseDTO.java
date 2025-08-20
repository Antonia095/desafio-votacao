package br.com.desafio.desafio_votacao.dto.response;

import br.com.desafio.desafio_votacao.dto.PautaDTO;
import br.com.desafio.desafio_votacao.enums.StatusSessaoVotacao;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class SessaoVotacaoResponseDTO {

  private Long id;

  private PautaDTO pauta;

  private LocalDateTime inicio;

  private LocalDateTime fim;

  private StatusSessaoVotacao statusSessaoVotacao;
}
