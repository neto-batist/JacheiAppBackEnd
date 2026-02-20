package com.ufape.jachei.service;

import com.ufape.jachei.dto.UsuarioRequest;
import com.ufape.jachei.models.PrestadorServico;
import com.ufape.jachei.models.Usuario;
import com.ufape.jachei.repo.PrestadorServicoRepo;
import com.ufape.jachei.repo.UsuarioRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.Set;

@Service
public class UsuarioService {

    private final UsuarioRepo usuarioRepo;
    private final PrestadorServicoRepo prestadorRepo;
    private final PasswordEncoder passwordEncoder;

    // Injeção de dependência via construtor
    public UsuarioService(UsuarioRepo usuarioRepo, PrestadorServicoRepo prestadorRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.prestadorRepo = prestadorRepo;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    public Usuario cadastrarUsuario(UsuarioRequest dto) {
        Optional<Usuario> existente = usuarioRepo.findByFirebaseUid(dto.getFirebaseUid());
        if (existente.isPresent()) {
            return existente.get();
        }

        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        usuario.setFirebaseUid(dto.getFirebaseUid());
        usuario.setLinkFoto(dto.getLinkFoto());

        // HASH DA SENHA: Nunca salvar texto puro no banco!
        if (dto.getSenha() != null && !dto.getSenha().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(dto.getSenha()));
        }

        return usuarioRepo.save(usuario);
    }

    // =========================================================================
    // NOVO: MÉTODO PARA A TELA DE CONFIGURAÇÕES (UPDATE)
    // =========================================================================
    @Transactional
    public Usuario atualizarDadosPerfil(String uid, String novoNome, String novaSenha) {
        Usuario usuario = usuarioRepo.findByFirebaseUid(uid)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (novoNome != null && !novoNome.trim().isEmpty()) {
            usuario.setNome(novoNome);
        }

        // Se o usuário quiser trocar a senha, fazemos o hash da nova
        if (novaSenha != null && !novaSenha.trim().isEmpty()) {
            usuario.setSenha(passwordEncoder.encode(novaSenha));
        }

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

    public Usuario atualizarFotoPerfil(String uid, MultipartFile arquivo) {
        Usuario usuario = buscarPorUid(uid)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (arquivo.isEmpty()) {
            throw new RuntimeException("O arquivo de imagem não pode estar vazio.");
        }

        try {
            // 1. Garante que a pasta física existe
            String diretorioUpload = "uploads/";
            Path caminhoUpload = Paths.get(diretorioUpload);
            if (!Files.exists(caminhoUpload)) {
                Files.createDirectories(caminhoUpload);
            }

            // 2. RECICLAGEM (Apaga a foto antiga caso seja um arquivo do nosso servidor)
            String fotoAtual = usuario.getLinkFoto();
            if (fotoAtual != null && fotoAtual.contains("/uploads/")) {
                // Pega apenas o nome final do arquivo que está na URL (ex: "uid_12345.jpg")
                String nomeArquivoAntigo = fotoAtual.substring(fotoAtual.lastIndexOf("/") + 1);
                Path caminhoArquivoAntigo = caminhoUpload.resolve(nomeArquivoAntigo);

                // Deleta do HD, liberando espaço
                Files.deleteIfExists(caminhoArquivoAntigo);
            }

            // 3. Prepara o nome do NOVO arquivo
            String nomeOriginal = arquivo.getOriginalFilename();
            String extensao = "";
            if (nomeOriginal != null && nomeOriginal.contains(".")) {
                extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            }

            // Adicionamos o "System.currentTimeMillis()" para criar um nome de arquivo ÚNICO.
            // Isso força o Flutter a baixar a imagem nova e não usar o cache antigo!
            String novoNomeArquivo = uid + "_" + System.currentTimeMillis() + extensao;
            Path caminhoFisicoArquivo = caminhoUpload.resolve(novoNomeArquivo);

            // 4. Salva a nova foto no HD
            Files.copy(arquivo.getInputStream(), caminhoFisicoArquivo, StandardCopyOption.REPLACE_EXISTING);

            // 5. Monta a URL nova para o banco de dados
            String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
            String urlDaFoto = baseUrl + "/uploads/" + novoNomeArquivo;

            usuario.setLinkFoto(urlDaFoto);
            return usuarioRepo.save(usuario);

        } catch (Exception e) {
            throw new RuntimeException("Falha ao salvar a imagem no servidor", e);
        }
    }
}