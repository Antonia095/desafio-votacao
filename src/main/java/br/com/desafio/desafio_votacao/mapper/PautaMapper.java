package br.com.desafio.desafio_votacao.mapper;

import br.com.desafio.desafio_votacao.dto.PautaDTO;
import br.com.desafio.desafio_votacao.dto.response.PautaResponseDTO;
import br.com.desafio.desafio_votacao.model.Pauta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PautaMapper {

  @Mapping(target = "quantidadeVotos", expression = "java(0)")
  @Mapping(target = "statusPauta", expression = "java(br.com.desafio.desafio_votacao.enums.StatusPauta.CRIADA)")
  Pauta toPauta(PautaDTO pautaDTO);

  PautaResponseDTO toPautaResponse(Pauta pauta);

}
