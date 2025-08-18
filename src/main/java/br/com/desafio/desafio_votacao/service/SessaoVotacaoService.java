package br.com.desafio.desafio_votacao.service;

import br.com.desafio.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.desafio.desafio_votacao.dto.response.SessaoVotacaoResponseDTO;
import br.com.desafio.desafio_votacao.exception.BusinessException;
import br.com.desafio.desafio_votacao.mapper.SessaoVotacaoMapper;
import br.com.desafio.desafio_votacao.model.Pauta;
import br.com.desafio.desafio_votacao.repository.PautaRepository;
import br.com.desafio.desafio_votacao.repository.SessaoVotacaoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SessaoVotacaoService {

  private final SessaoVotacaoRepository sessaoRepository;
  private final PautaRepository pautaRepository;

  private final SessaoVotacaoMapper mapper;

  @Transactional
  public SessaoVotacaoResponseDTO criarSessaoVotacao(SessaoVotacaoDTO sessaoVotacaoDTO) {

    var pauta = pautaRepository
        .findById(sessaoVotacaoDTO.getIdPauta())
        .orElseThrow(() -> new EntityNotFoundException("Não foi encontrada pauta!"));


    var sessaoVotacao =mapper.toSessaoVotacao(sessaoVotacaoDTO, pauta);

    try {
      sessaoRepository.save(sessaoVotacao);
    } catch (Exception e) {

      log.error("Erro ao salvar sessão de votação: {}", e.getMessage());

      throw new BusinessException("Erro ao salvar sessão de votação");
    }

    return mapper.toSessaoVotacaoResponse(sessaoVotacao);

  }

  public List<SessaoVotacaoResponseDTO> listarSessoesVotacao() {

    return sessaoRepository.findAll()
        .stream()
        .map(mapper::toSessaoVotacaoResponse)
        .collect(Collectors.toList());

  }

  public SessaoVotacaoResponseDTO buscarSessaoVotacaoPorId(Long id) {

    var sessaoVotacao = sessaoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Sessão de votação não encontrada!"));

    return mapper.toSessaoVotacaoResponse(sessaoVotacao);
  }

  public void deletarSessaoVotacao(Long id) {

    var sessaoVotacao = sessaoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Sessão de votação não encontrada!"));

    sessaoRepository.delete(sessaoVotacao);

    log.info("Sessão de votação excluida com sucesso!");
  }

}
