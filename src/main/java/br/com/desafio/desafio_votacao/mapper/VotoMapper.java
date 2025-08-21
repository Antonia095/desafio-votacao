package br.com.desafio.desafio_votacao.mapper;

import br.com.desafio.desafio_votacao.dto.VotoDTO;
import br.com.desafio.desafio_votacao.dto.response.VotoResponseDTO;
import br.com.desafio.desafio_votacao.model.SessaoVotacao;
import br.com.desafio.desafio_votacao.model.Voto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VotoMapper {

  @Mapping(target = "sessaoVotacao", source = "sessaoVotacao")
  Voto toVoto(VotoDTO votoDTO, SessaoVotacao sessaoVotacao);

  @Mapping(target = "idVoto", source = "id")
  @Mapping(target = "idSessaoVotacao", source = "sessaoVotacao.id")
  VotoResponseDTO toVotoResponse(Voto voto);

}
