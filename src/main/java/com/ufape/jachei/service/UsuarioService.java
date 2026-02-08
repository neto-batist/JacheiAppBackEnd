package com.ufape.jachei.service;

import com.ufape.jachei.dto.UsuarioRequest;
import com.ufape.jachei.models.PrestadorServico;
import com.ufape.jachei.models.Usuario;
import com.ufape.jachei.repo.PrestadorServicoRepo;
import com.ufape.jachei.repo.UsuarioRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepo usuarioRepo;
    private final PrestadorServicoRepo prestadorRepo;

    // Injeção de dependência via construtor
    public UsuarioService(UsuarioRepo usuarioRepo, PrestadorServicoRepo prestadorRepo) {
        this.usuarioRepo = usuarioRepo;
        this.prestadorRepo = prestadorRepo;
    }

    /**
     * Cadastra um novo usuário ou retorna o existente se já houver login
     */
    @Transactional
    public Usuario cadastrarUsuario(UsuarioRequest dto) {
        // Verifica se já existe pelo UID do Firebase para evitar duplicação
        Optional<Usuario> existente = usuarioRepo.findByFirebaseUid(dto.getFirebaseUid());
        if (existente.isPresent()) {
            return existente.get();
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setFirebaseUid(dto.getFirebaseUid());
        usuario.setLinkFoto(dto.getLinkFoto());

        return usuarioRepo.save(usuario);
    }

    /**
     * Busca um usuário pelo UID (usado no perfil)
     */
    public Optional<Usuario> buscarPorUid(String uid) {
        return usuarioRepo.findByFirebaseUid(uid);
    }

    /**
     * Adiciona ou remove um prestador dos favoritos (Toggle)
     */
    @Transactional
    public void alternarFavorito(String uidUsuario, Long idPrestador) {
        Usuario usuario = usuarioRepo.findByFirebaseUid(uidUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com UID: " + uidUsuario));

        PrestadorServico prestador = prestadorRepo.findById(idPrestador)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado com ID: " + idPrestador));

        // Lógica de Toggle: Se tem, tira. Se não tem, põe.
        if (usuario.getFavoritos().contains(prestador)) {
            usuario.getFavoritos().remove(prestador);
        } else {
            usuario.getFavoritos().add(prestador);
        }

        usuarioRepo.save(usuario);
    }

    /**
     * Lista os favoritos garantindo que a lista seja carregada do banco
     */
    @Transactional(readOnly = true)
    public Set<PrestadorServico> listarFavoritos(String uidUsuario) {
        Usuario usuario = usuarioRepo.findByFirebaseUid(uidUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // O método .size() força o Hibernate a buscar os dados da tabela de relacionamento
        // antes de fechar a transação, evitando o erro "LazyInitializationException"
        usuario.getFavoritos().size();

        return usuario.getFavoritos();
    }
}