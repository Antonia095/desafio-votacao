package br.com.desafio.desafio_votacao.unitaries.service;

import static br.com.desafio.desafio_votacao.enums.StatusPauta.CRIADA;
import static br.com.desafio.desafio_votacao.enums.StatusPauta.EM_VOTACAO;
import static br.com.desafio.desafio_votacao.enums.StatusPauta.REPROVADA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import br.com.desafio.desafio_votacao.dto.PautaDTO;
import br.com.desafio.desafio_votacao.dto.response.PautaResponseDTO;
import br.com.desafio.desafio_votacao.exception.BusinessException;
import br.com.desafio.desafio_votacao.exception.EntityNotFoundException;
import br.com.desafio.desafio_votacao.mapper.PautaMapper;
import br.com.desafio.desafio_votacao.model.Pauta;
import br.com.desafio.desafio_votacao.repository.PautaRepository;
import br.com.desafio.desafio_votacao.service.PautaService;
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
class PautaServiceTest {

  @Mock
  private PautaRepository pautaRepository;

  @Mock
  private PautaMapper mapper;

  @InjectMocks
  private PautaService pautaService;

  private PautaDTO pautaDTO;

  private Pauta pauta;

  private PautaResponseDTO pautaResponseDTO;

  @BeforeEach
  void setUp() {

    pautaDTO = new PautaDTO();
    pautaDTO.setTitulo("Título");
    pautaDTO.setDescricao("Descrição");

    pauta = new Pauta();
    pauta.setId(1L);
    pauta.setTitulo("Título");
    pauta.setDescricao("Descrição");
    pauta.setStatus(CRIADA);

    pautaResponseDTO = new PautaResponseDTO();
    pautaResponseDTO.setId(1L);
    pautaResponseDTO.setTitulo("Título");
    pautaResponseDTO.setDescricao("Descrição");
    pautaResponseDTO.setStatusPauta(CRIADA);

  }

  @Test
  @DisplayName("Deve cadastrar pauta com sucesso!")
  void deveCadastrarPautaComSucesso() {

    when(mapper.toPauta(pautaDTO)).thenReturn(pauta);
    when(pautaRepository.save(pauta)).thenReturn(pauta);
    when(mapper.toPautaResponse(pauta)).thenReturn(pautaResponseDTO);

    var response = pautaService.cadastrarPauta(pautaDTO);

    assertEquals("Título", response.getTitulo());
    assertEquals("Descrição", response.getDescricao());
    verify(pautaRepository).save(pauta);
  }

  @Test
  @DisplayName("Deve lançar BusinessException ao salvar pauta")
  void deveLancarBusinessExceptionAoSalvarPauta() {

    when(mapper.toPauta(pautaDTO)).thenReturn(pauta);
    when(pautaRepository.save(pauta)).thenThrow(new RuntimeException("Erro"));

    assertThrows(BusinessException.class, () -> pautaService.cadastrarPauta(pautaDTO));
  }

  @Test
  @DisplayName("Deve listar pautas com sucesso")
  void deveListarPautas() {

    when(pautaRepository.findAll()).thenReturn(List.of(pauta));
    when(mapper.toPautaResponse(pauta)).thenReturn(pautaResponseDTO);

    var resultado = pautaService.listarPautas();

    assertEquals(1, resultado.size());
    assertEquals("Título", resultado.get(0).getTitulo());
  }

  @Test
  @DisplayName("Deve buscar pauta por Id com sucesso")
  void deveBuscarPautaPorId() {

    when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
    when(mapper.toPautaResponse(pauta)).thenReturn(pautaResponseDTO);

    var resultado = pautaService.buscarPautaPorId(1L);

    assertEquals("Título", resultado.getTitulo());
  }

  @Test
  @DisplayName("Deve lançar EntityNotFoundExceptionNotFoundException ao buscar pauta por id")
  void deveLancarEntityNotFoundExceptionAoBuscarPautaPorId() {

    when(pautaRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> pautaService.buscarPautaPorId(1L));
  }

  @Test
  @DisplayName("Deve atualizar pauta com sucesso")
  void deveAtualizarPautaComSucesso() {

    var pautaAtualizada = new Pauta();
    pautaAtualizada.setTitulo("Novo Titulo");
    pautaAtualizada.setDescricao("Nova Descrição");

    var responseDTO = new PautaResponseDTO();
    responseDTO.setTitulo("Novo Titulo");
    responseDTO.setDescricao("Nova Descrição");

    when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));
    when(pautaRepository.save(pauta)).thenReturn(pautaAtualizada);
    when(mapper.toPautaResponse(pautaAtualizada)).thenReturn(responseDTO);

    var resultado = pautaService.atualizarPauta(1L, pautaDTO);

    assertEquals("Novo Titulo", resultado.getTitulo());
  }

  @Test
  @DisplayName("Não deve atualizar pauta quando estiver em votação ou reprovada")
  void naoDeveAtualizarPautaEmVotacaoOuReprovada() {

    pauta.setStatus(EM_VOTACAO);

    when(pautaRepository.findById(1L)).thenReturn(Optional.of(pauta));

    assertThrows(BusinessException.class, () -> pautaService.atualizarPauta(1L, pautaDTO));

    pauta.setStatus(REPROVADA);
    assertThrows(BusinessException.class, () -> pautaService.atualizarPauta(1L, pautaDTO));
  }

  @Test
  @DisplayName("Deve deletar pauta com sucesso")
  void deveDeletarPautaComSucesso() {

    var id = 1L;

    when(pautaRepository.findById(id)).thenReturn(Optional.of(pauta));

    pautaService.deletarPauta(id);

    verify(pautaRepository).delete(pauta);
  }

  @Test
  @DisplayName("Deve lançar EntityNotFoundExceptionNotFoundException ao deletar pauta")
  void deveLancarEntityNotFoundExceptionAoDeletarPauta() {

    var id = 1L;

    when(pautaRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> pautaService.deletarPauta(id));
  }
}
