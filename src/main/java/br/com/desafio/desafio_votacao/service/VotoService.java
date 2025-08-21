package br.com.desafio.desafio_votacao.service;

import static br.com.desafio.desafio_votacao.enums.StatusSessaoVotacao.EM_ANDAMENTO;

import br.com.desafio.desafio_votacao.dto.VotoDTO;
import br.com.desafio.desafio_votacao.dto.response.VotoResponseDTO;
import br.com.desafio.desafio_votacao.exception.BusinessException;
import br.com.desafio.desafio_votacao.exception.EntityNotFoundException;
import br.com.desafio.desafio_votacao.mapper.VotoMapper;
import br.com.desafio.desafio_votacao.model.SessaoVotacao;
import br.com.desafio.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.desafio.desafio_votacao.repository.VotoRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotoService {

  private final VotoRepository votoRepository;
  private final SessaoVotacaoRepository sessaoVotacaoRepository;
  private final VotoMapper mapper;

  @Transactional
  public VotoResponseDTO registrarVoto(VotoDTO votoDTO) {

    var sessaoVotacao = validarSessaoVotacao(votoDTO.getIdSessaoVotacao());

    validarVotoUnico(votoDTO.getIdUsuario(), votoDTO.getIdSessaoVotacao());

    var voto = mapper.toVoto(votoDTO, sessaoVotacao);

    try {
      votoRepository.save(voto);
      log.info("Voto registrado com sucesso. ID: {}", voto.getId());
    } catch (Exception e) {
      log.error("Erro ao salvar voto: {}", e.getMessage());
      throw new BusinessException("Erro ao registrar voto");
    }

    return mapper.toVotoResponse(voto);
  }

  public List<VotoResponseDTO> listarVotosPorSessao(Long idSessaoVotacao) {

    validarSessaoExiste(idSessaoVotacao);

    var votos = votoRepository.buscarVotosPorSessao(idSessaoVotacao);

    return votos.stream()
        .map(mapper::toVotoResponse)
        .collect(Collectors.toList());
  }

  private SessaoVotacao validarSessaoVotacao(Long idSessaoVotacao) {

    var sessaoVotacao = validarSessaoExiste(idSessaoVotacao);

    if (sessaoVotacao.getStatusSessaoVotacao() != EM_ANDAMENTO) {
      throw new BusinessException("Sessão de votação esta finalizada, portanto não é possível registrar votos");
    }

    var agora = LocalDateTime.now();

    if (agora.isAfter(sessaoVotacao.getFim())) {
      throw new BusinessException("Período de votação não permitido");
    }

    return sessaoVotacao;
  }

  private void validarVotoUnico(Long idUsuario, Long idSessaoVotacao) {

    if (votoRepository.validarVotacaoUnicaParaUsuario(idUsuario, idSessaoVotacao)) {
      throw new BusinessException("Usuário já votou nesta sessão");
    }
  }

  private SessaoVotacao validarSessaoExiste(Long idSessaoVotacao) {

    return sessaoVotacaoRepository.findById(idSessaoVotacao)
        .orElseThrow(() -> new EntityNotFoundException("Sessão de votação não encontrada!"));
  }
}
