package br.com.desafio.desafio_votacao.mapper;

import br.com.desafio.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.desafio.desafio_votacao.dto.response.SessaoVotacaoResponseDTO;
import br.com.desafio.desafio_votacao.model.Pauta;
import br.com.desafio.desafio_votacao.model.SessaoVotacao;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SessaoVotacaoMapper {

  @Mapping(target = "pauta", source = "pauta")
  @Mapping(target = "inicio", source = "inicioSessao")
  @Mapping(target = "fim", source = "fimSessao")
  @Mapping(target = "statusSessaoVotacao", expression = "java(br.com.desafio.desafio_votacao.enums.StatusSessaoVotacao.EM_ANDAMENTO)")
  SessaoVotacao toSessaoVotacao(SessaoVotacaoDTO sessaoVotacaoDTO,
      Pauta pauta, LocalDateTime inicioSessao, LocalDateTime fimSessao);

  SessaoVotacaoResponseDTO toSessaoVotacaoResponse(SessaoVotacao sessaoVotacao);

}
