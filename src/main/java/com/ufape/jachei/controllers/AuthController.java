package com.ufape.jachei.controllers;

import com.ufape.jachei.config.JwtService;
import com.ufape.jachei.dto.AuthResponse;
import com.ufape.jachei.dto.LoginRequest;
import com.ufape.jachei.models.Usuario;
import com.ufape.jachei.repo.UsuarioRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepo usuarioRepo;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService, UsuarioRepo usuarioRepo) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarioRepo = usuarioRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        // 1. O Spring verifica se a senha bate com a do banco (faz o hash internamente)
        // Se a senha estiver errada, ele joga uma exceção automaticamente aqui (Status 403)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        // 2. Se passou, o cara existe. Vamos buscar ele no banco para gerar o token e pegar a foto
        Usuario usuario = usuarioRepo.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 3. Gera o Token Matemático
        String jwtToken = jwtService.generateToken(usuario);

        // 4. Monta a resposta pro Flutter
        AuthResponse response = AuthResponse.builder()
                .token(jwtToken)
                .firebaseUid(usuario.getFirebaseUid())
                .nome(usuario.getNome())
                .linkFoto(usuario.getLinkFoto())
                .build();

        return ResponseEntity.ok(response);
    }
}