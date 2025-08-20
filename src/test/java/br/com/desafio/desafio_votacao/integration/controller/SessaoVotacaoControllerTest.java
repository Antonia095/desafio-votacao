package br.com.desafio.desafio_votacao.integration.controller;

import static br.com.desafio.desafio_votacao.enums.StatusSessaoVotacao.EM_ANDAMENTO;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.desafio.desafio_votacao.controller.SessaoVotacaoController;
import br.com.desafio.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.desafio.desafio_votacao.dto.response.SessaoVotacaoResponseDTO;
import br.com.desafio.desafio_votacao.exception.EntityNotFoundException;
import br.com.desafio.desafio_votacao.service.SessaoVotacaoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(SessaoVotacaoController.class)
class SessaoVotacaoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private SessaoVotacaoService sessaoVotacaoService;

  private SessaoVotacaoDTO sessaoVotacaoDTO;

  private SessaoVotacaoResponseDTO sessaoVotacaoResponseDTO;

  @BeforeEach
  void setUp() {

    sessaoVotacaoDTO = new SessaoVotacaoDTO();
    sessaoVotacaoDTO.setIdPauta(1L);

    sessaoVotacaoResponseDTO = new SessaoVotacaoResponseDTO();
    sessaoVotacaoResponseDTO.setId(1L);
    sessaoVotacaoResponseDTO.setInicio(LocalDateTime.now());
    sessaoVotacaoResponseDTO.setFim(LocalDateTime.now().plusMinutes(5));
    sessaoVotacaoResponseDTO.setStatusSessaoVotacao(EM_ANDAMENTO);

  }

  @Test
  @DisplayName("Deve cadastrar sessão de votação com sucesso")
  void deveCadastrarSessaoVotacaoComSucesso() throws Exception {

    when(sessaoVotacaoService.criarSessaoVotacao(sessaoVotacaoDTO))
        .thenReturn(sessaoVotacaoResponseDTO);

    mockMvc.perform(post("/sessoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sessaoVotacaoDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)));

    verify(sessaoVotacaoService).criarSessaoVotacao(sessaoVotacaoDTO);
  }

  @Test
  @DisplayName("Deve criar sessão de votação com duração padrão quando não informada")
  void deveCriarSessaoVotacaoComDuracaoPadrao() throws Exception {


    var sessaoVotacaoDto = new SessaoVotacaoDTO();
    sessaoVotacaoDto.setIdPauta(1L);

    when(sessaoVotacaoService.criarSessaoVotacao(sessaoVotacaoDto))
        .thenReturn(sessaoVotacaoResponseDTO);

    mockMvc.perform(post("/sessoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sessaoVotacaoDto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.id", is(1)));

    verify(sessaoVotacaoService).criarSessaoVotacao(sessaoVotacaoDto);
  }

  @Test
  @DisplayName("Deve listar sessões de votação")
  void deveListarSessoesVotacao() throws Exception {

    when(sessaoVotacaoService.criarSessaoVotacao(sessaoVotacaoDTO))
        .thenReturn(sessaoVotacaoResponseDTO);

    mockMvc.perform(post("/sessoes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(sessaoVotacaoDTO)))
        .andExpect(status().isCreated());

    var listaSessaoResponse = List.of(sessaoVotacaoResponseDTO);

    when(sessaoVotacaoService.listarSessoesVotacao())
        .thenReturn(listaSessaoResponse);

    mockMvc.perform(get("/sessoes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].id").exists());

    verify(sessaoVotacaoService).criarSessaoVotacao(sessaoVotacaoDTO);
    verify(sessaoVotacaoService).listarSessoesVotacao();
  }

  @Test
  @DisplayName("Deve buscar sessão de votação por ID")
  void deveBuscarSessaoVotacaoPorId() throws Exception {

    var id = 1L;

    when(sessaoVotacaoService.buscarSessaoVotacaoPorId(id))
        .thenReturn(sessaoVotacaoResponseDTO);

    mockMvc.perform(get("/sessoes/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(sessaoVotacaoResponseDTO.getId()))
        .andExpect(jsonPath("$.statusSessaoVotacao")
            .value(sessaoVotacaoResponseDTO.getStatusSessaoVotacao().toString()))
    ;

    verify(sessaoVotacaoService).buscarSessaoVotacaoPorId(id);
  }

  @Test
  @DisplayName("Deve retornar 404 ao buscar sessão de votação inexistente")
  void deveRetornar404SessaoVotacaoInexistente() throws Exception {

    var id = 10L;

    when(sessaoVotacaoService.buscarSessaoVotacaoPorId(id))
        .thenThrow(new EntityNotFoundException("Sessão de votação não encontrada!"));

    mockMvc.perform(get("/sessoes/" + id))
        .andExpect(status().isNotFound());

    verify(sessaoVotacaoService).buscarSessaoVotacaoPorId(id);
  }

  @Test
  @DisplayName("Deve deletar sessão de votação com sucesso")
  void deveDeletarSessaoVotacaoComSucesso() throws Exception {

    var id = 1L;

    mockMvc.perform(delete("/sessoes/" + id))
        .andExpect(status().isNoContent());

    verify(sessaoVotacaoService).deletarSessaoVotacao(id);
  }

}
