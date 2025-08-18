package br.com.desafio.desafio_votacao.repository;

import br.com.desafio.desafio_votacao.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {

}
