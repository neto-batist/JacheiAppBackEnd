package com.ufape.jachei.controllers;

import com.ufape.jachei.dto.AvaliacaoRequest;
import com.ufape.jachei.models.Avaliacao;
import com.ufape.jachei.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/avaliacoes")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;

    public AvaliacaoController(AvaliacaoService avaliacaoService) {
        this.avaliacaoService = avaliacaoService;
    }

    @PostMapping
    public ResponseEntity<Avaliacao> criar(
            @Valid @RequestBody AvaliacaoRequest dto,
            @AuthenticationPrincipal UserDetails userDetails) { // <--- Injeta a Identidade Real

        String emailLogado = userDetails.getUsername();
        Avaliacao nova = avaliacaoService.cadastrarAvaliacao(dto, emailLogado);

        return ResponseEntity.status(201).body(nova);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        String emailLogado = userDetails.getUsername();
        avaliacaoService.deletarAvaliacao(id, emailLogado);

        return ResponseEntity.noContent().build();
    }
}