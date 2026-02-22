package com.ufape.jachei.controllers;

import com.ufape.jachei.dto.PrestadorDetalhadoResponse;
import com.ufape.jachei.dto.PrestadorRequest;
import com.ufape.jachei.dto.PrestadorSearchFilter;
import com.ufape.jachei.dto.PrestadorSimplesResponse;
import com.ufape.jachei.service.PrestadorService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/prestadores")
public class PrestadorController {

    private final PrestadorService prestadorService;

    public PrestadorController(PrestadorService prestadorService) {
        this.prestadorService = prestadorService;
    }

    @PostMapping
    public ResponseEntity<PrestadorDetalhadoResponse> criar(
            @Valid @RequestBody PrestadorRequest dto,
            @AuthenticationPrincipal UserDetails userDetails) { // <--- JWT Injetado

        // Passamos o email seguro para o Service fazer o "Upgrade"
        PrestadorDetalhadoResponse novo = prestadorService.cadastrarPrestador(dto, userDetails.getUsername());
        return ResponseEntity.status(201).body(novo);
    }

    // ===============================================================================
    // DÍVIDA RESOLVIDA: Rota limpa para o Painel do Prestador
    // Ex: GET /api/prestadores/me
    // ===============================================================================
    @GetMapping("/me")
    public ResponseEntity<PrestadorDetalhadoResponse> buscarMeuPainel(
            @AuthenticationPrincipal UserDetails userDetails) {

        return prestadorService.buscarMeuPainel(userDetails.getUsername())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @PutMapping("/me")
    public ResponseEntity<PrestadorDetalhadoResponse> atualizarPerfil(
            @Valid @RequestBody PrestadorRequest dto,
            @AuthenticationPrincipal UserDetails userDetails) {

        PrestadorDetalhadoResponse atualizado = prestadorService.atualizarPrestador(dto, userDetails.getUsername());
        return ResponseEntity.ok(atualizado);
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

    @PutMapping("/me/{uid}/servico-principal/{idServico}")
    public ResponseEntity<Void> definirServicoPrincipal(
            @PathVariable String uid,
            @PathVariable Long idServico,
            @AuthenticationPrincipal UserDetails userDetails) {

        prestadorService.definirServicoPrincipal(uid, idServico, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @PostMapping(value = "/me/{uid}/fotos-trabalho", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> adicionarFotoPortifolio(
            @PathVariable String uid,
            @RequestParam("foto") MultipartFile foto,
            @AuthenticationPrincipal UserDetails userDetails) {

        prestadorService.adicionarFotoPortifolio(uid, foto, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/me/{uid}/fotos-trabalho/{idFoto}")
    public ResponseEntity<Void> removerFotoPortifolio(
            @PathVariable String uid,
            @PathVariable Long idFoto,
            @AuthenticationPrincipal UserDetails userDetails) {

        prestadorService.removerFotoPortifolio(uid, idFoto, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }
}