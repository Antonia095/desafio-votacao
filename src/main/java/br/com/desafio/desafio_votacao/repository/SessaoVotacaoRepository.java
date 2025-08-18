package br.com.desafio.desafio_votacao.repository;

import br.com.desafio.desafio_votacao.model.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, Long> {

}
