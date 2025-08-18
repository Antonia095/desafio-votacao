package br.com.desafio.desafio_votacao.controller;

import static org.springframework.http.HttpStatus.CREATED;

import br.com.desafio.desafio_votacao.dto.PautaDTO;
import br.com.desafio.desafio_votacao.dto.response.PautaResponseDTO;
import br.com.desafio.desafio_votacao.service.PautaService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pautas")
@RequiredArgsConstructor
public class PautaController {

  private final PautaService service;

  @PostMapping
  public ResponseEntity<PautaResponseDTO> cadastrarPauta(@RequestBody PautaDTO dto) {

   var pautaResponse = service.cadastrarPauta(dto);

   return ResponseEntity.status(CREATED).body(pautaResponse);
  }

  @GetMapping
  public ResponseEntity<List<PautaResponseDTO>> listarPauta() {

    return ResponseEntity.ok(service.listarPautas());
  }

  @GetMapping("/{id}")
  public ResponseEntity<PautaResponseDTO> buscarPautaPorId(@PathVariable Long id) {

    return ResponseEntity.ok(service.buscarPautaPorId(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<PautaResponseDTO> atualizarPauta(@PathVariable Long id,
      @Valid @RequestBody PautaDTO dto) {

    return ResponseEntity.ok(service.atualizarPauta(id, dto));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deletarPauta(@PathVariable Long id) {

    service.deletarPauta(id);

    return ResponseEntity.noContent().build();
  }
}
