package com.ufape.jachei.controllers;

import com.ufape.jachei.dto.AvaliacaoRequest;
import com.ufape.jachei.models.Avaliacao;
import com.ufape.jachei.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    public ResponseEntity<Avaliacao> criar(@Valid @RequestBody AvaliacaoRequest dto) {
        Avaliacao nova = avaliacaoService.avaliar(dto);
        return ResponseEntity.status(201).body(nova);
    }

    @GetMapping("/prestador/{id}")
    public ResponseEntity<List<Avaliacao>> listarDoPrestador(@PathVariable Long id) {
        return ResponseEntity.ok(avaliacaoService.listarPorPrestador(id));
    }
}