package com.ufape.jachei.controllers;

import com.ufape.jachei.dto.PrestadorRequest;
import com.ufape.jachei.models.PrestadorServico;
import com.ufape.jachei.service.PrestadorService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestadores")
public class PrestadorController {

    private final PrestadorService prestadorService;

    public PrestadorController(PrestadorService prestadorService) {
        this.prestadorService = prestadorService;
    }

    @PostMapping
    public ResponseEntity<PrestadorServico> criar(@Valid @RequestBody PrestadorRequest dto) {
        PrestadorServico novo = prestadorService.cadastrarPrestador(dto);
        return ResponseEntity.status(201).body(novo);
    }

    @GetMapping("/proximos")
    public ResponseEntity<List<PrestadorServico>> buscarProximos(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double raioKm) {

        List<PrestadorServico> lista = prestadorService.buscarProximos(lat, lng, raioKm);
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/me/{uid}")
    public ResponseEntity<PrestadorServico> buscarMeuPerfil(@PathVariable String uid) {
        return prestadorService.buscarPorUid(uid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/{uid}/servicos/{idServico}")
    public ResponseEntity<Void> adicionarServico(@PathVariable String uid, @PathVariable Long idServico) {
        prestadorService.adicionarServico(uid, idServico);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/{uid}/servicos/{idServico}")
    public ResponseEntity<Void> removerServico(@PathVariable String uid, @PathVariable Long idServico) {
        prestadorService.removerServico(uid, idServico);
        return ResponseEntity.ok().build();
    }
}