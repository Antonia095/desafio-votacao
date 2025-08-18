package br.com.desafio.desafio_votacao.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import br.com.desafio.desafio_votacao.enums.TipoVoto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Voto {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @ManyToOne
  private Pauta pauta;

  private Long idUsuario;

  private TipoVoto tipoVoto;

}
