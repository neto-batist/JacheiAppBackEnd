package com.ufape.jachei.controllers;

import com.ufape.jachei.dto.PrestadorSimplesResponse; // <--- Import atualizado
import com.ufape.jachei.dto.UsuarioRequest;
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

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@Valid @RequestBody UsuarioRequest dto) {
        Usuario usuario = usuarioService.cadastrarUsuario(dto);
        return ResponseEntity.status(201).body(usuario);
    }

    @GetMapping("/me/{uid}")
    public ResponseEntity<Usuario> buscarPerfil(@PathVariable String uid) {
        return usuarioService.buscarPorUid(uid)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/me/{uid}/favoritos/{idPrestador}")
    public ResponseEntity<Void> favoritar(@PathVariable String uid, @PathVariable Long idPrestador) {
        usuarioService.alternarFavorito(uid, idPrestador);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me/{uid}/favoritos")
    public ResponseEntity<List<PrestadorSimplesResponse>> listarFavoritos(@PathVariable String uid) {
        Set<PrestadorServico> favoritos = usuarioService.listarFavoritos(uid);

        // Converte a lista de Entidades para DTOs (Mais leves e seguros)
        // Como estamos listando especificamente os favoritos deste usuário, passamos "true" por padrão
        List<PrestadorSimplesResponse> resposta = favoritos.stream()
                .map(prestador -> PrestadorSimplesResponse.fromEntity(prestador, true))
                .collect(Collectors.toList());

        return ResponseEntity.ok(resposta);
    }

    @PostMapping(value = "/me/{uid}/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Usuario> atualizarFotoPerfil(
            @PathVariable String uid,
            @RequestParam("foto") MultipartFile foto) {

        Usuario usuarioAtualizado = usuarioService.atualizarFotoPerfil(uid, foto);
        return ResponseEntity.ok(usuarioAtualizado);
    }
}