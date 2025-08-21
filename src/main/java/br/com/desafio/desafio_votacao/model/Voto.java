package br.com.desafio.desafio_votacao.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import br.com.desafio.desafio_votacao.enums.TipoVoto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "votos", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"id_usuario", "id_sessao_votacao"})
})
public class Voto {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "id_usuario", nullable = false)
  private Long idUsuario;

  @ManyToOne
  @JoinColumn(name = "id_sessao_votacao", nullable = false)
  private SessaoVotacao sessaoVotacao;

  private TipoVoto tipoVoto;

}
