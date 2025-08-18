package br.com.desafio.desafio_votacao.repository;

import br.com.desafio.desafio_votacao.model.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, Long> {

}
