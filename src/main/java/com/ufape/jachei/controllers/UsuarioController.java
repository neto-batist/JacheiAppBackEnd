package com.ufape.jachei.controllers;

import com.ufape.jachei.dto.PrestadorSimplesResponse; // <--- Import atualizado
import com.ufape.jachei.dto.UsuarioRequest;
import com.ufape.jachei.dto.UsuarioResponse;
import com.ufape.jachei.models.PrestadorServico;
import com.ufape.jachei.models.Usuario;
import com.ufape.jachei.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<UsuarioResponse> cadastrar(@Valid @RequestBody UsuarioRequest dto) {
        Usuario usuario = usuarioService.cadastrarUsuario(dto);
        return ResponseEntity.status(201).body(UsuarioResponse.fromEntity(usuario));
    }

    @GetMapping("/me/{uid}")
    public ResponseEntity<UsuarioResponse> buscarPerfil(@PathVariable String uid) {
        return usuarioService.buscarPorUid(uid)
                .map(UsuarioResponse::fromEntity)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/{uid}/favoritos/{idPrestador}")
    public ResponseEntity<Void> favoritar(
            @PathVariable String uid,
            @PathVariable Long idPrestador,
            @AuthenticationPrincipal UserDetails userDetails) {
        usuarioService.alternarFavorito(uid, idPrestador, userDetails.getUsername());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/{uid}/favoritos")
    public ResponseEntity<List<PrestadorSimplesResponse>> listarFavoritos(
            @PathVariable String uid,
            @AuthenticationPrincipal UserDetails userDetails) {
        Set<PrestadorServico> favoritos = usuarioService.listarFavoritos(uid, userDetails.getUsername());

        List<PrestadorSimplesResponse> resposta = favoritos.stream()
                .map(prestador -> PrestadorSimplesResponse.fromEntity(prestador, true))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

    @PostMapping(value = "/me/{uid}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UsuarioResponse> atualizarFotoPerfil(
            @PathVariable String uid,
            @RequestParam("foto") MultipartFile foto,
            @AuthenticationPrincipal UserDetails userDetails) {

        Usuario usuarioAtualizado = usuarioService.atualizarFotoPerfil(uid, foto, userDetails.getUsername());
        return ResponseEntity.ok(UsuarioResponse.fromEntity(usuarioAtualizado));
    }
}