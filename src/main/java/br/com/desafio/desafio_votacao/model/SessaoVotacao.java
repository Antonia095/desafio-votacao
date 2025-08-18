package br.com.desafio.desafio_votacao.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import br.com.desafio.desafio_votacao.enums.StatusSessaoVotacao;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SessaoVotacao {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToOne
  private Pauta pauta;

  private LocalDateTime inicio;

  private LocalDateTime fim;

  private StatusSessaoVotacao statusSessaoVotacao;

}
