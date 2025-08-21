package br.com.desafio.desafio_votacao.integration.controller;

import static br.com.desafio.desafio_votacao.enums.StatusPauta.CRIADA;
import static br.com.desafio.desafio_votacao.enums.StatusSessaoVotacao.EM_ANDAMENTO;
import static br.com.desafio.desafio_votacao.enums.TipoVoto.SIM;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.desafio.desafio_votacao.controller.VotoController;
import br.com.desafio.desafio_votacao.dto.VotoDTO;
import br.com.desafio.desafio_votacao.dto.response.VotoResponseDTO;
import br.com.desafio.desafio_votacao.exception.BusinessException;
import br.com.desafio.desafio_votacao.model.Pauta;
import br.com.desafio.desafio_votacao.model.SessaoVotacao;
import br.com.desafio.desafio_votacao.service.VotoService;
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

@WebMvcTest(VotoController.class)
class VotoControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private VotoService votoService;

  private Pauta pauta;
  private SessaoVotacao sessaoVotacao;
  private VotoDTO votoDTO;
  private VotoResponseDTO votoResponseDTO;

  @BeforeEach
  void setUp() {

    pauta = new Pauta();
    pauta.setId(30L);
    pauta.setTitulo("Título");
    pauta.setDescricao("Descrição");
    pauta.setStatus(CRIADA);

    sessaoVotacao = new SessaoVotacao();
    sessaoVotacao.setId(20L);
    sessaoVotacao.setPauta(pauta);
    sessaoVotacao.setInicio(LocalDateTime.now().minusMinutes(5));
    sessaoVotacao.setFim(LocalDateTime.now().plusMinutes(10));
    sessaoVotacao.setStatusSessaoVotacao(EM_ANDAMENTO);

    votoDTO = new VotoDTO();
    votoDTO.setIdUsuario(123L);
    votoDTO.setIdSessaoVotacao(sessaoVotacao.getId());
    votoDTO.setTipoVoto(SIM);

    votoResponseDTO = new VotoResponseDTO();
    votoResponseDTO.setIdVoto(40L);
    votoResponseDTO.setIdUsuario(123L);
    votoResponseDTO.setIdSessaoVotacao(sessaoVotacao.getId());
    votoResponseDTO.setTipoVoto(SIM);
  }

  @Test
  @DisplayName("Deve registrar voto com sucesso")
  void deveRegistrarVotoComSucesso() throws Exception {

    when(votoService.registrarVoto(votoDTO)).thenReturn(votoResponseDTO);

    mockMvc.perform(post("/votos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(votoDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.idVoto").exists())
        .andExpect(jsonPath("$.idUsuario").value(123L))
        .andExpect(jsonPath("$.idSessaoVotacao").value(sessaoVotacao.getId()))
        .andExpect(jsonPath("$.tipoVoto").value("SIM"));

    verify(votoService).registrarVoto(votoDTO);
  }

  @Test
  @DisplayName("Deve retornar erro quando usuario ja votou")
  void deveRetornarErroQuandoUsuarioJaVotou() throws Exception {

    when(votoService.registrarVoto(votoDTO))
        .thenThrow(new BusinessException("Usuário já votou nesta sessão"));

    mockMvc.perform(post("/votos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(votoDTO)))
        .andExpect(status().isBadRequest());

    verify(votoService).registrarVoto(votoDTO);
  }

  @Test
  @DisplayName("Deve listar votos por sessão")
  void deveListarVotosPorSessao() throws Exception {

    when(votoService.registrarVoto(votoDTO)).thenReturn(votoResponseDTO);

    mockMvc.perform(post("/votos")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(votoDTO)))
        .andExpect(status().isCreated());

    var listaVotos = List.of(votoResponseDTO);

    when(votoService.listarVotosPorSessao(sessaoVotacao.getId())).thenReturn(listaVotos);

    mockMvc.perform(get("/votos/sessoes/" + sessaoVotacao.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].idUsuario").value(123L))
        .andExpect(jsonPath("$[0].tipoVoto").value("SIM"));

    verify(votoService).registrarVoto(votoDTO);
    verify(votoService).listarVotosPorSessao(sessaoVotacao.getId());
  }
}

