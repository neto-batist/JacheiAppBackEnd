package com.ufape.jachei.controllers;

import com.ufape.jachei.dto.PrestadorDetalhadoResponse;
import com.ufape.jachei.dto.PrestadorRequest;
import com.ufape.jachei.dto.PrestadorSearchFilter;
import com.ufape.jachei.dto.PrestadorSimplesResponse;
import com.ufape.jachei.service.PrestadorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prestadores")
public class PrestadorController {

    private final PrestadorService prestadorService;

    public PrestadorController(PrestadorService prestadorService) {
        this.prestadorService = prestadorService;
    }

    @PostMapping
    public ResponseEntity<PrestadorDetalhadoResponse> criar(@Valid @RequestBody PrestadorRequest dto) {
        PrestadorDetalhadoResponse novo = prestadorService.cadastrarPrestador(dto);
        return ResponseEntity.status(201).body(novo);
    }

    // ===============================================================================
    // ROTA CENTRAL: Pesquisa e Filtros (Onde o Flutter e a IA vão se conectar)
    // Ex: GET /api/prestadores?cidade=Garanhuns&atende24h=true&page=0&size=10
    // ===============================================================================
    @GetMapping
    public ResponseEntity<Page<PrestadorSimplesResponse>> listarPrestadores(
            @ModelAttribute PrestadorSearchFilter filtro,
            Pageable pageable,
            @AuthenticationPrincipal UserDetails userDetails) {

        String emailLogado = (userDetails != null) ? userDetails.getUsername() : null;
        Page<PrestadorSimplesResponse> pagina = prestadorService.buscarPrestadores(filtro, pageable, emailLogado);

        return ResponseEntity.ok(pagina);
    }

    // ===============================================================================
    // DADOS PARA A TELA DE PERFIL (Quando clicar num Card)
    // Ex: GET /api/prestadores/1
    // ===============================================================================
    @GetMapping("/{id}")
    public ResponseEntity<PrestadorDetalhadoResponse> buscarDetalhesDoPrestador(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        String emailLogado = (userDetails != null) ? userDetails.getUsername() : null;
        PrestadorDetalhadoResponse detalhes = prestadorService.buscarDetalhes(id, emailLogado);

        return ResponseEntity.ok(detalhes);
    }

    // Retorna o perfil gerenciável do usuário logado (Painel do Prestador)
    @GetMapping("/me/{uid}")
    public ResponseEntity<PrestadorDetalhadoResponse> buscarMeuPainel(@PathVariable String uid) {
        return prestadorService.buscarMeuPerfil(uid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/{uid}/servicos/{idServico}")
    public ResponseEntity<Void> adicionarServico(
            @PathVariable String uid,
            @PathVariable Long idServico,
            @AuthenticationPrincipal UserDetails userDetails) {

        prestadorService.adicionarServico(uid, idServico, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/{uid}/servicos/{idServico}")
    public ResponseEntity<Void> removerServico(
            @PathVariable String uid,
            @PathVariable Long idServico,
            @AuthenticationPrincipal UserDetails userDetails) {

        prestadorService.removerServico(uid, idServico, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}