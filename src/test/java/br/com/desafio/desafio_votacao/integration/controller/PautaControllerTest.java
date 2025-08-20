package br.com.desafio.desafio_votacao.integration.controller;

import static br.com.desafio.desafio_votacao.enums.StatusPauta.CRIADA;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import br.com.desafio.desafio_votacao.controller.PautaController;
import br.com.desafio.desafio_votacao.dto.PautaDTO;
import br.com.desafio.desafio_votacao.dto.response.PautaResponseDTO;
import br.com.desafio.desafio_votacao.exception.EntityNotFoundException;
import br.com.desafio.desafio_votacao.service.PautaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PautaController.class)
class PautaControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private PautaService pautaService;

  private PautaDTO pautaDTO;

  private PautaResponseDTO pautaResponseDTO;

  @BeforeEach
  void setUp() {

    pautaDTO = new PautaDTO();
    pautaDTO.setTitulo("Título");
    pautaDTO.setDescricao("Descrição");

    pautaResponseDTO = new PautaResponseDTO();
    pautaResponseDTO.setId(1L);
    pautaResponseDTO.setTitulo("Título");
    pautaResponseDTO.setDescricao("Descrição");
    pautaResponseDTO.setStatusPauta(CRIADA);

  }

  @Test
  @DisplayName("Deve cadastrar pauta com sucesso")
  void deveCadastrarPautaComSucesso() throws Exception {

    when(pautaService.cadastrarPauta(pautaDTO)).thenReturn(pautaResponseDTO);

    mockMvc.perform(post("/pautas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pautaDTO)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.titulo").value("Título"))
        .andExpect(jsonPath("$.descricao").value("Descrição"))
        .andExpect(jsonPath("$.id").exists());

    verify(pautaService).cadastrarPauta(pautaDTO);
  }


  @Test
  @DisplayName("Deve listar pautas")
  void deveListarPautas() throws Exception {

    when(pautaService.cadastrarPauta(pautaDTO)).thenReturn(pautaResponseDTO);

    mockMvc.perform(post("/pautas")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pautaDTO)))
        .andExpect(status().isCreated());

    var listaPautaResponseDTO = List.of(pautaResponseDTO);

    when(pautaService.listarPautas()).thenReturn(listaPautaResponseDTO);

    mockMvc.perform(get("/pautas"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].titulo").value("Título"));

    verify(pautaService).cadastrarPauta(pautaDTO);
    verify(pautaService).listarPautas();

  }

  @Test
  @DisplayName("Deve buscar pauta por ID")
  void deveBuscarPautaPorId() throws Exception {

    var id = 1L;

    when(pautaService.buscarPautaPorId(id)).thenReturn(pautaResponseDTO);

    mockMvc.perform(get("/pautas/" + id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(id))
        .andExpect(jsonPath("$.titulo").value("Título"));

    verify(pautaService).buscarPautaPorId(id);
  }

  @Test
  @DisplayName("Deve retornar 404 ao buscar pauta inexistente")
  void deveRetornar404PautaInexistente() throws Exception {

    var id = 10L;

    when(pautaService.buscarPautaPorId(id))
        .thenThrow(new EntityNotFoundException("Pauta não encontrada"));

    mockMvc.perform(get("/pautas/" + id))
        .andExpect(status().isNotFound());

    verify(pautaService).buscarPautaPorId(id);
  }

  @Test
  @DisplayName("Deve atualizar pauta com sucesso")
  void deveAtualizarPautaComSucesso() throws Exception {

    var id = 1L;

    var pautaAtualizadaDTO = new PautaDTO();
    pautaAtualizadaDTO.setTitulo("Título Atualizada");
    pautaAtualizadaDTO.setDescricao("Descrição atualizada");

    var pautaAtualizadaResponseDTO = new PautaResponseDTO();
    pautaAtualizadaResponseDTO.setId(id);
    pautaAtualizadaResponseDTO.setTitulo("Título Atualizada");
    pautaAtualizadaResponseDTO.setDescricao("Descrição atualizada");

    when(pautaService.atualizarPauta(id, pautaAtualizadaDTO)).thenReturn(pautaAtualizadaResponseDTO);

    mockMvc.perform(put("/pautas/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(pautaAtualizadaDTO)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.titulo").value("Título Atualizada"))
        .andExpect(jsonPath("$.descricao").value("Descrição atualizada"));

    verify(pautaService).atualizarPauta(id, pautaAtualizadaDTO);
  }

  @Test
  @DisplayName("Deve deletar pauta com sucesso")
  void deveDeletarPautaComSucesso() throws Exception {

    var id = 1L;

    mockMvc.perform(delete("/pautas/" + id))
        .andExpect(status().isNoContent());

    verify(pautaService).deletarPauta(id);
  }

}
