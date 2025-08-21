package br.com.desafio.desafio_votacao.unitaries.service;

import static br.com.desafio.desafio_votacao.enums.StatusSessaoVotacao.EM_ANDAMENTO;
import static br.com.desafio.desafio_votacao.enums.StatusSessaoVotacao.FINALIZADA;
import static br.com.desafio.desafio_votacao.enums.TipoVoto.SIM;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.desafio.desafio_votacao.dto.VotoDTO;
import br.com.desafio.desafio_votacao.dto.response.VotoResponseDTO;
import br.com.desafio.desafio_votacao.exception.BusinessException;
import br.com.desafio.desafio_votacao.exception.EntityNotFoundException;
import br.com.desafio.desafio_votacao.mapper.VotoMapper;
import br.com.desafio.desafio_votacao.model.SessaoVotacao;
import br.com.desafio.desafio_votacao.model.Voto;
import br.com.desafio.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.desafio.desafio_votacao.repository.VotoRepository;
import br.com.desafio.desafio_votacao.service.VotoService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

  @Mock
  private VotoRepository votoRepository;

  @Mock
  private SessaoVotacaoRepository sessaoVotacaoRepository;

  @Mock
  private VotoMapper mapper;

  @InjectMocks
  private VotoService votoService;

  private VotoDTO votoDTO;
  private Voto voto;
  private VotoResponseDTO votoResponseDTO;
  private SessaoVotacao sessaoVotacao;

  @BeforeEach
  void setUp() {
    votoDTO = new VotoDTO();
    votoDTO.setIdUsuario(1L);
    votoDTO.setIdSessaoVotacao(1L);
    votoDTO.setTipoVoto(SIM);

    sessaoVotacao = new SessaoVotacao();
    sessaoVotacao.setId(1L);
    sessaoVotacao.setStatusSessaoVotacao(EM_ANDAMENTO);
    sessaoVotacao.setInicio(LocalDateTime.now().minusMinutes(10));
    sessaoVotacao.setFim(LocalDateTime.now().plusMinutes(10));

    voto = new Voto();
    voto.setId(1L);
    voto.setIdUsuario(1L);
    voto.setSessaoVotacao(sessaoVotacao);
    voto.setTipoVoto(SIM);

    votoResponseDTO = new VotoResponseDTO();
    votoResponseDTO.setIdVoto(1L);
    votoResponseDTO.setIdUsuario(1L);
    votoResponseDTO.setIdSessaoVotacao(1L);
    votoResponseDTO.setTipoVoto(SIM);
  }

  @Test
  @DisplayName("Deve registrar voto com sucesso")
  void deveRegistrarVotoComSucesso() {

    when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoVotacao));
    when(votoRepository.existsByIdUsuarioAndSessaoVotacaoId(1L, 1L)).thenReturn(false);
    when(mapper.toVoto(votoDTO, sessaoVotacao)).thenReturn(voto);
    when(votoRepository.save(voto)).thenReturn(voto);
    when(mapper.toVotoResponse(voto)).thenReturn(votoResponseDTO);

    var resultado = votoService.registrarVoto(votoDTO);

    assertNotNull(resultado);
    assertEquals(1L, resultado.getIdVoto());
    assertEquals(1L, resultado.getIdUsuario());
    assertEquals(SIM, resultado.getTipoVoto());

    verify(sessaoVotacaoRepository).findById(1L);
    verify(votoRepository).existsByIdUsuarioAndSessaoVotacaoId(1L, 1L);
    verify(mapper).toVoto(votoDTO, sessaoVotacao);
    verify(votoRepository).save(voto);
    verify(mapper).toVotoResponse(voto);
  }

  @Test
  @DisplayName("Deve lançar BusinessException quando erro ao salvar voto")
  void deveLancarBusinessExceptionErroAoSalvarVoto() {

    when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoVotacao));
    when(votoRepository.existsByIdUsuarioAndSessaoVotacaoId(1L, 1L)).thenReturn(false);
    when(mapper.toVoto(votoDTO, sessaoVotacao)).thenReturn(voto);
    when(votoRepository.save(voto)).thenThrow(new RuntimeException("Erro de banco"));

    var exception = assertThrows(BusinessException.class,
        () -> votoService.registrarVoto(votoDTO));

    assertEquals("Erro ao registrar voto", exception.getMessage());

    verify(votoRepository).save(voto);
    verify(mapper, never()).toVotoResponse(any());
  }

  @Test
  @DisplayName("Deve lançar EntityNotFoundException quando sessão de votação não existir")
  void deveLancarEntityNotFoundExceptionSessaoVotacaoNaoExiste() {

    when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.empty());

    var exception = assertThrows(EntityNotFoundException.class,
        () -> votoService.registrarVoto(votoDTO));

    assertEquals("Sessão de votação não encontrada!", exception.getMessage());

    verify(sessaoVotacaoRepository).findById(1L);
    verify(votoRepository, never()).existsByIdUsuarioAndSessaoVotacaoId(any(), any());
    verify(votoRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve lançar BusinessException quando sessão de votação não estiver em andamento")
  void deveLancarBusinessExceptionSessaoVotacaoNaoEmAndamento() {

    sessaoVotacao.setStatusSessaoVotacao(FINALIZADA);
    when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoVotacao));

    var exception = assertThrows(BusinessException.class,
        () -> votoService.registrarVoto(votoDTO));

    assertEquals("Sessão de votação esta finalizada, portanto não é possível registrar votos",
        exception.getMessage());

    verify(sessaoVotacaoRepository).findById(1L);
    verify(votoRepository, never()).existsByIdUsuarioAndSessaoVotacaoId(any(), any());
  }

  @Test
  @DisplayName("Deve lançar BusinessException quando estiver fora do período de votação - após o fim")
  void deveLancarBusinessExceptionForaDoPeriodoVotacao() {

    sessaoVotacao.setInicio(LocalDateTime.now().minusMinutes(20));
    sessaoVotacao.setFim(LocalDateTime.now().minusMinutes(10));
    when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoVotacao));

    var exception = assertThrows(BusinessException.class,
        () -> votoService.registrarVoto(votoDTO));

    assertEquals("Período de votação não permitido", exception.getMessage());

    verify(sessaoVotacaoRepository).findById(1L);
    verify(votoRepository, never()).existsByIdUsuarioAndSessaoVotacaoId(any(), any());
  }

  @Test
  @DisplayName("Deve lançar BusinessException quando usuário já votou")
  void deveLancarBusinessExceptionQuandoUsuarioJaVotou() {

    when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoVotacao));
    when(votoRepository.existsByIdUsuarioAndSessaoVotacaoId(1L, 1L)).thenReturn(true);

    var exception = assertThrows(BusinessException.class,
        () -> votoService.registrarVoto(votoDTO));

    assertEquals("Usuário já votou nesta sessão", exception.getMessage());

    verify(sessaoVotacaoRepository).findById(1L);
    verify(votoRepository).existsByIdUsuarioAndSessaoVotacaoId(1L, 1L);
    verify(votoRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve listar votos por sessão com sucesso")
  void deveListarVotosPorSessaoComSucesso() {

    var votos = List.of(voto);
    when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoVotacao));
    when(votoRepository.findBySessaoVotacaoId(1L)).thenReturn(votos);
    when(mapper.toVotoResponse(voto)).thenReturn(votoResponseDTO);

    var resultado = votoService.listarVotosPorSessao(1L);

    assertNotNull(resultado);
    assertEquals(1, resultado.size());
    assertEquals(1L, resultado.get(0).getIdVoto());

    verify(sessaoVotacaoRepository).findById(1L);
    verify(votoRepository).findBySessaoVotacaoId(1L);
    verify(mapper).toVotoResponse(voto);
  }

  @Test
  @DisplayName("Deve retornar lista vazia quando não há votos na sessão")
  void deveRetornarListaVaziaQuandoNaoHaVotos() {

    when(sessaoVotacaoRepository.findById(1L)).thenReturn(Optional.of(sessaoVotacao));
    when(votoRepository.findBySessaoVotacaoId(1L)).thenReturn(Collections.emptyList());

    var resultado = votoService.listarVotosPorSessao(1L);

    assertNotNull(resultado);
    assertTrue(resultado.isEmpty());

    verify(sessaoVotacaoRepository).findById(1L);
    verify(votoRepository).findBySessaoVotacaoId(1L);
    verify(mapper, never()).toVotoResponse(any());
  }

}
