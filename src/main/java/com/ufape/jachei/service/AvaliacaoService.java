package com.ufape.jachei.service;

import com.ufape.jachei.dto.AvaliacaoRequest;
import com.ufape.jachei.models.Avaliacao;
import com.ufape.jachei.models.PrestadorServico;
import com.ufape.jachei.models.Usuario;
import com.ufape.jachei.repo.AvaliacaoRepo;
import com.ufape.jachei.repo.PrestadorServicoRepo;
import com.ufape.jachei.repo.UsuarioRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AvaliacaoService {

    private final AvaliacaoRepo avaliacaoRepo;
    private final PrestadorServicoRepo prestadorRepo;
    private final UsuarioRepo usuarioRepo;

    public AvaliacaoService(AvaliacaoRepo avaliacaoRepo, PrestadorServicoRepo prestadorRepo, UsuarioRepo usuarioRepo) {
        this.avaliacaoRepo = avaliacaoRepo;
        this.prestadorRepo = prestadorRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional
    public Avaliacao avaliar(AvaliacaoRequest dto) {
        PrestadorServico prestador = prestadorRepo.findById(dto.getIdPrestador())
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        Usuario usuario = usuarioRepo.findByFirebaseUid(dto.getUidUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setPrestador(prestador);
        avaliacao.setUsuario(usuario);
        avaliacao.setNota(dto.getNota());
        avaliacao.setDescricao(dto.getDescricao());

        Avaliacao salva = avaliacaoRepo.save(avaliacao);

        // --- GATILHO DE MÉDIA ---
        Double novaMedia = avaliacaoRepo.calcularMediaDoPrestador(prestador.getId());
        prestador.setMediaAvaliacoes(novaMedia);
        prestadorRepo.save(prestador);

        return salva;
    }

    public List<Avaliacao> listarPorPrestador(Long idPrestador) {
        return avaliacaoRepo.findByPrestadorId(idPrestador);
    }
}