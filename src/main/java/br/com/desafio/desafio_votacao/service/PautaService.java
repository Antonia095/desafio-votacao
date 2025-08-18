package br.com.desafio.desafio_votacao.service;

import br.com.desafio.desafio_votacao.dto.PautaDTO;
import br.com.desafio.desafio_votacao.dto.response.PautaResponseDTO;
import br.com.desafio.desafio_votacao.mapper.PautaMapper;
import br.com.desafio.desafio_votacao.repository.PautaRepository;
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

    pautaRepository.save(pauta);

    return mapper.toPautaResponse(pauta);

  }

}
