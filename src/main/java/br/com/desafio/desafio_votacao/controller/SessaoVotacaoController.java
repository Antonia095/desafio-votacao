package br.com.desafio.desafio_votacao.controller;

import static org.springframework.http.HttpStatus.CREATED;

import br.com.desafio.desafio_votacao.dto.SessaoVotacaoDTO;
import br.com.desafio.desafio_votacao.dto.response.SessaoVotacaoResponseDTO;
import br.com.desafio.desafio_votacao.service.SessaoVotacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessoes")
@RequiredArgsConstructor
@Tag(name = "Sessão de Votação", description = "Endpoints relacionado a sessões de votação")
public class SessaoVotacaoController {

  private final SessaoVotacaoService service;

  @PostMapping
  @Operation(summary = "Cadastrar sessão de votação")
  public ResponseEntity<SessaoVotacaoResponseDTO> cadastrarSessaoVotacao(@RequestBody SessaoVotacaoDTO dto) {

   var sessaoVotacaoResponse = service.criarSessaoVotacao(dto);

   return ResponseEntity.status(CREATED).body(sessaoVotacaoResponse);
  }

  @GetMapping
  @Operation(summary = "Listar sessão de votação")
  public ResponseEntity<List<SessaoVotacaoResponseDTO>> listarSessoesVotacao() {

    return ResponseEntity.ok(service.listarSessoesVotacao());
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar sessão de votação por ID")
  public ResponseEntity<SessaoVotacaoResponseDTO> buscarSessaoVotacaoPorId(@PathVariable Long id) {

    return ResponseEntity.ok(service.buscarSessaoVotacaoPorId(id));
  }

  @DeleteMapping("/{id}")
  @Operation(summary = "Deletar sessão de votação")
  public ResponseEntity<Void> deletarSessaoVotacao(@PathVariable Long id) {

    service.deletarSessaoVotacao(id);

    return ResponseEntity.noContent().build();
  }
}
