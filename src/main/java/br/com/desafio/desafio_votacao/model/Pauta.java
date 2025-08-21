package br.com.desafio.desafio_votacao.model;

import static br.com.desafio.desafio_votacao.enums.StatusPauta.CRIADA;
import static jakarta.persistence.GenerationType.IDENTITY;

import br.com.desafio.desafio_votacao.enums.StatusPauta;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pautas")
public class Pauta {

  @Id
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  private String titulo;

  private String descricao;

  @Enumerated(EnumType.STRING)
  private StatusPauta status = CRIADA;

}
