package br.com.desafio.desafio_votacao.model;

import static jakarta.persistence.GenerationType.IDENTITY;

import br.com.desafio.desafio_votacao.enums.StatusPauta;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Pauta {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String titulo;

  private String descricao;

  private int quantidadeVotos;

  private StatusPauta statusPauta;

}
