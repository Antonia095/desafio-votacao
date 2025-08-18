package br.com.desafio.desafio_votacao.mapper;

import br.com.desafio.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.desafio.desafio_votacao.dto.response.SessaoVotacaoResponseDTO;
import br.com.desafio.desafio_votacao.model.Pauta;
import br.com.desafio.desafio_votacao.model.SessaoVotacao;
import java.time.LocalDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SessaoVotacaoMapper {

  @Mapping(target = "pauta", source = "pauta")
  @Mapping(target = "inicio", expression = "java(calcularInicioSessao(sessaoVotacaoDTO))")
  @Mapping(target = "fim", expression = "java(calcularFimDaSessao(sessaoVotacaoDTO))")
  @Mapping(target = "statusSessaoVotacao", expression = "java(br.com.desafio.desafio_votacao.enums.StatusSessaoVotacao.EM_ANDAMENTO)")
  SessaoVotacao toSessaoVotacao(SessaoVotacaoDTO sessaoVotacaoDTO, Pauta pauta);

  SessaoVotacaoResponseDTO toSessaoVotacaoResponse(SessaoVotacao sessaoVotacao);

  @Named("calcularInicioSessao")
  static LocalDateTime calcularInicioSessao(SessaoVotacaoDTO dto) {

    return dto.getInicio() != null ? dto.getInicio() : LocalDateTime.now();
  }

  @Named("calcularFimDaSessao")
  static LocalDateTime calcularFimDaSessao(SessaoVotacaoDTO dto) {
    if(dto.getFim() != null) {

      return dto.getFim();
    }

    var duracaoSessao = validarDuracaoSessaoVotacao(dto.getDuracaoSessaoMinutos());
    if (dto.getInicio() != null) {

      return dto.getInicio().plusMinutes(duracaoSessao);
    }

    return LocalDateTime.now().plusMinutes(duracaoSessao);
  }

  static Integer validarDuracaoSessaoVotacao(Integer duracaoSessao) {

    return duracaoSessao != null ? duracaoSessao : 1;
  }

}
