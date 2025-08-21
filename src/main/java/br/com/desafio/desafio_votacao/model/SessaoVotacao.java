package br.com.desafio.desafio_votacao.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import br.com.desafio.desafio_votacao.enums.StatusSessaoVotacao;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sessoes")
public class SessaoVotacao {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "id_pauta", nullable = false)
  private Pauta pauta;

  private LocalDateTime inicio;

  private LocalDateTime fim;

  @Column(name = "status")
  private StatusSessaoVotacao statusSessaoVotacao;

}
