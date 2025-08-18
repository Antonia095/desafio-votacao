package br.com.desafio.desafio_votacao.controller;

import static org.springframework.http.HttpStatus.CREATED;

import br.com.desafio.desafio_votacao.dto.PautaDTO;
import br.com.desafio.desafio_votacao.dto.response.PautaResponseDTO;
import br.com.desafio.desafio_votacao.service.PautaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}
