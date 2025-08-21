package br.com.desafio.desafio_votacao.repository;

import br.com.desafio.desafio_votacao.model.Voto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

  boolean validarVotacaoUnicaParaUsuario(Long idUsuario, Long idSessaoVotacao);

  List<Voto> buscarVotosPorSessao(Long idSessaoVotacao);
}
