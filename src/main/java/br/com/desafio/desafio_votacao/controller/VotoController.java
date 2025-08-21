package br.com.desafio.desafio_votacao.controller;

import static org.springframework.http.HttpStatus.CREATED;

import br.com.desafio.desafio_votacao.dto.VotoDTO;
import br.com.desafio.desafio_votacao.dto.response.VotoResponseDTO;
import br.com.desafio.desafio_votacao.service.VotoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/votos")
@RequiredArgsConstructor
@Tag(name = "Votos", description = "Endpoints relacionados ao registro e consulta de votos")
public class VotoController {

  private final VotoService votoService;

  @PostMapping
  @Operation(summary = "Registrar voto")
  public ResponseEntity<VotoResponseDTO> registrarVoto(
      @Valid @RequestBody VotoDTO votoDTO) {

    var votoResponse = votoService.registrarVoto(votoDTO);

    return ResponseEntity.status(CREATED).body(votoResponse);
  }

  @GetMapping("/sessoes/{idSessaoVotacao}")
  @Operation(summary = "Listar votos por sessão")
  public ResponseEntity<List<VotoResponseDTO>> listarVotosPorSessao(@PathVariable Long idSessaoVotacao) {

    var votos = votoService.listarVotosPorSessao(idSessaoVotacao);

    return ResponseEntity.ok(votos);
  }
}
