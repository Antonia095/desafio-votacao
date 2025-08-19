package br.com.desafio.desafio_votacao.service;

import static br.com.desafio.desafio_votacao.enums.StatusPauta.CRIADA;
import static br.com.desafio.desafio_votacao.enums.StatusPauta.EM_VOTACAO;
import static br.com.desafio.desafio_votacao.enums.StatusPauta.REPROVADA;

import br.com.desafio.desafio_votacao.dto.PautaDTO;
import br.com.desafio.desafio_votacao.dto.response.PautaResponseDTO;
import br.com.desafio.desafio_votacao.exception.BusinessException;
import br.com.desafio.desafio_votacao.exception.EntityNotFoundException;
import br.com.desafio.desafio_votacao.mapper.PautaMapper;
import br.com.desafio.desafio_votacao.model.Pauta;
import br.com.desafio.desafio_votacao.repository.PautaRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PautaService {

  private final PautaRepository pautaRepository;
  private final PautaMapper mapper;

  public PautaResponseDTO cadastrarPauta(PautaDTO pautaDTO) {

    log.info("Castrando pauta: {}", pautaDTO.getTitulo());

    var pauta = mapper.toPauta(pautaDTO);
    pauta.setStatus(CRIADA);

    try {
      pautaRepository.save(pauta);
    } catch (Exception e) {

      log.error("Erro ao salvar pauta: {}", e.getMessage());
      throw new BusinessException("Erro ao salvar pauta");
    }

    return mapper.toPautaResponse(pauta);
  }

  public List<PautaResponseDTO> listarPautas() {

    return pautaRepository.findAll()
        .stream()
        .map(mapper::toPautaResponse)
        .collect(Collectors.toList());
  }

  public PautaResponseDTO buscarPautaPorId(Long id) {

    var pauta = buscarPauta(id);

    return mapper.toPautaResponse(pauta);
  }

  public PautaResponseDTO atualizarPauta(Long id, PautaDTO dto) {

    var pauta = buscarPauta(id);

    if (pauta.getStatus() == EM_VOTACAO || pauta.getStatus() == REPROVADA) {
      throw new BusinessException(
          "Não é possível atualizar pauta que esteja em votação ou que foi reprovada"
      );
    }

    pauta.setTitulo(dto.getTitulo());
    pauta.setDescricao(dto.getDescricao());

    var pautaAtualizada = pautaRepository.save(pauta);

    return mapper.toPautaResponse(pautaAtualizada);
  }

  public void deletarPauta(Long id) {

    var pauta = buscarPauta(id);

    pautaRepository.delete(pauta);

    log.info("Pauta deletada com sucesso!");
  }


  private Pauta buscarPauta(Long id) {

    return pautaRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Pauta não encontrada!"));
  }
}
