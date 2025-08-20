package br.com.desafio.desafio_votacao.unitaries.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.desafio.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.desafio.desafio_votacao.dto.response.SessaoVotacaoResponseDTO;
import br.com.desafio.desafio_votacao.exception.BusinessException;
import br.com.desafio.desafio_votacao.exception.EntityNotFoundException;
import br.com.desafio.desafio_votacao.mapper.SessaoVotacaoMapper;
import br.com.desafio.desafio_votacao.model.Pauta;
import br.com.desafio.desafio_votacao.model.SessaoVotacao;
import br.com.desafio.desafio_votacao.repository.SessaoVotacaoRepository;
import br.com.desafio.desafio_votacao.service.PautaService;
import br.com.desafio.desafio_votacao.service.SessaoVotacaoService;
import java.time.LocalDateTime;
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
class SessaoVotacaoServiceTest {

  @Mock
  private SessaoVotacaoRepository sessaoRepository;

  @Mock
  private SessaoVotacaoMapper mapper;

  @Mock
  private PautaService pautaService;

  @InjectMocks
  private SessaoVotacaoService sessaoVotacaoService;

  private SessaoVotacaoDTO sessaoVotacaoDTO;
  private Pauta pauta;
  private SessaoVotacao sessaoVotacao;
  private SessaoVotacaoResponseDTO sessaoVotacaoResponseDTO;

  private Long ID = 10L;

  @BeforeEach
  void setUp() {
    sessaoVotacaoDTO = new SessaoVotacaoDTO();
    sessaoVotacaoDTO.setIdPauta(ID);

    pauta = new Pauta();
    pauta.setId(ID);
    pauta.setTitulo("Pauta Teste");

    sessaoVotacao = new SessaoVotacao();
    sessaoVotacao.setId(ID);
    sessaoVotacao.setPauta(pauta);

    sessaoVotacaoResponseDTO = new SessaoVotacaoResponseDTO();
    sessaoVotacaoResponseDTO.setId(ID);
  }

  @Test
  @DisplayName("Deve criar sessão de votação com sucesso")
  void deveCriarSessaoVotacaoComSucesso() {

    when(pautaService.buscarPauta(ID)).thenReturn(pauta);
    when(mapper.toSessaoVotacao(any(), any(), any(), any())).thenReturn(sessaoVotacao);
    when(sessaoRepository.save(sessaoVotacao)).thenReturn(sessaoVotacao);
    when(mapper.toSessaoVotacaoResponse(sessaoVotacao)).thenReturn(sessaoVotacaoResponseDTO);

    var resultado = sessaoVotacaoService.criarSessaoVotacao(sessaoVotacaoDTO);

    assertNotNull(resultado);
    assertEquals(ID, resultado.getId());
    verify(pautaService).buscarPauta(ID);
    verify(sessaoRepository).save(sessaoVotacao);
    verify(mapper).toSessaoVotacaoResponse(sessaoVotacao);
  }

  @Test
  @DisplayName("Deve lançar EntityNotFoundException quando não existir pauta")
  void deveLancarEntityNotFoundExceptionNaoExistirPauta() {

    when(pautaService.buscarPauta(ID))
        .thenThrow(new EntityNotFoundException("Pauta não encontrada!"));;

    assertThrows(EntityNotFoundException.class,
        () -> sessaoVotacaoService.criarSessaoVotacao(sessaoVotacaoDTO));

    verify(pautaService).buscarPauta(ID);
    verify(sessaoRepository, never()).save(any());
  }

  @Test
  @DisplayName("Deve lançar BusinessException quando houver erro ao salvar sessão de votação")
  void deveLancarBusinessExceptionQuandoHouverErroAoSalvarSessao() {

    when(pautaService.buscarPauta(ID)).thenReturn(pauta);
    when(mapper.toSessaoVotacao(any(), any(), any(), any())).thenReturn(sessaoVotacao);
    when(sessaoRepository.save(sessaoVotacao)).thenThrow(new RuntimeException("Erro ao salvar votação"));

    assertThrows(BusinessException.class,
        () -> sessaoVotacaoService.criarSessaoVotacao(sessaoVotacaoDTO));

    verify(sessaoRepository).save(sessaoVotacao);
  }

  @Test
  @DisplayName("Deve listar sessões de votação")
  void deveListarSessoesVotacao() {

    var sessoes = List.of(sessaoVotacao);
    when(sessaoRepository.findAll()).thenReturn(sessoes);
    when(mapper.toSessaoVotacaoResponse(sessaoVotacao)).thenReturn(sessaoVotacaoResponseDTO);

    var resultado = sessaoVotacaoService.listarSessoesVotacao();

    assertEquals(1, resultado.size());
    assertEquals(ID, resultado.get(0).getId());
    verify(sessaoRepository).findAll();
    verify(mapper).toSessaoVotacaoResponse(sessaoVotacao);
  }

  @Test
  @DisplayName("Deve buscar sessão por ID com sucesso")
  void deveBuscarSessaoPorIdComSucesso() {

    when(sessaoRepository.findById(ID)).thenReturn(Optional.of(sessaoVotacao));
    when(mapper.toSessaoVotacaoResponse(sessaoVotacao)).thenReturn(sessaoVotacaoResponseDTO);

    var resultado = sessaoVotacaoService.buscarSessaoVotacaoPorId(ID);

    assertNotNull(resultado);
    assertEquals(ID, resultado.getId());
    verify(sessaoRepository).findById(ID);
    verify(mapper).toSessaoVotacaoResponse(sessaoVotacao);
  }

  @Test
  @DisplayName("Deve lançar EntityNotFoundException ao buscar sessão inexistente")
  void deveLancarEntityNotFoundExceptionSessaoInexistente() {

    when(sessaoRepository.findById(ID)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> sessaoVotacaoService.buscarSessaoVotacaoPorId(ID));

    verify(sessaoRepository).findById(ID);
  }

  @Test
  @DisplayName("Deve deletar sessão com sucesso")
  void deveDeletarSessaoComSucesso() {

    when(sessaoRepository.findById(ID)).thenReturn(Optional.of(sessaoVotacao));

    sessaoVotacaoService.deletarSessaoVotacao(ID);

    verify(sessaoRepository).findById(ID);
    verify(sessaoRepository).delete(sessaoVotacao);
  }

  @Test
  @DisplayName("Deve lançar EntityNotFoundException ao deletar sessão inexistente")
  void deveLancarEntityNotFoundExceptionAoDeletarSessaoInexistente() {

    when(sessaoRepository.findById(ID)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class,
        () -> sessaoVotacaoService.deletarSessaoVotacao(ID));

    verify(sessaoRepository).findById(ID);
    verify(sessaoRepository, never()).delete(any());
  }

  @Test
  @DisplayName("Deve calcular início da sessão corretamente")
  void deveCalcularInicioSessaoCorretamente() {

    var inicioSessao = LocalDateTime.of(2025, 10, 19, 10, 0);
    sessaoVotacaoDTO.setInicio(inicioSessao);

    when(pautaService.buscarPauta(ID)).thenReturn(pauta);
    when(mapper.toSessaoVotacao(any(), any(), eq(inicioSessao), any())).thenReturn(sessaoVotacao);
    when(sessaoRepository.save(sessaoVotacao)).thenReturn(sessaoVotacao);
    when(mapper.toSessaoVotacaoResponse(sessaoVotacao)).thenReturn(sessaoVotacaoResponseDTO);

    sessaoVotacaoService.criarSessaoVotacao(sessaoVotacaoDTO);

    verify(mapper).toSessaoVotacao(any(), any(), eq(inicioSessao), any());
  }

  @Test
  @DisplayName("Deve retornar fim da sessão com duração padrão quando não é adicionado fim da sessão")
  void deveCalcularFimSessaoComDuracaoPadraoQuandoNaoEhAdicionadoFimSessao() {

    var dto = new SessaoVotacaoDTO();
    dto.setIdPauta(ID);
    dto.setInicio(LocalDateTime.of(2025, 10, 19, 10, 0));

    when(pautaService.buscarPauta(ID)).thenReturn(pauta);
    when(mapper.toSessaoVotacao(any(), any(), any(), any())).thenReturn(sessaoVotacao);
    when(sessaoRepository.save(sessaoVotacao)).thenReturn(sessaoVotacao);
    when(mapper.toSessaoVotacaoResponse(sessaoVotacao)).thenReturn(sessaoVotacaoResponseDTO);

    sessaoVotacaoService.criarSessaoVotacao(dto);

    var inicio = dto.getInicio();
    var fimSessaoEsperado = LocalDateTime.of(2025, 10, 19, 10, 1);

    verify(mapper).toSessaoVotacao(eq(dto), eq(pauta), eq(inicio), eq(fimSessaoEsperado));
  }

  @Test
  @DisplayName("Deve usar fim específico quando fornecido")
  void deveUsarFimEspecificoQuandoFornecido() {

    var fimSessao = LocalDateTime.of(2025, 10, 19, 11, 0);
    sessaoVotacaoDTO.setFim(fimSessao);

    when(pautaService.buscarPauta(ID)).thenReturn(pauta);
    when(mapper.toSessaoVotacao(any(), any(), any(), eq(fimSessao))).thenReturn(sessaoVotacao);
    when(sessaoRepository.save(sessaoVotacao)).thenReturn(sessaoVotacao);
    when(mapper.toSessaoVotacaoResponse(sessaoVotacao)).thenReturn(sessaoVotacaoResponseDTO);

    sessaoVotacaoService.criarSessaoVotacao(sessaoVotacaoDTO);

    verify(mapper).toSessaoVotacao(any(), any(), any(), eq(fimSessao));
  }
}
