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
    public Avaliacao cadastrarAvaliacao(AvaliacaoRequest dto, String emailAvaliador) {
        Usuario usuario = usuarioRepo.findByEmail(emailAvaliador)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        PrestadorServico prestador = prestadorRepo.findById(dto.getIdPrestador())
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado."));

        // Dívida Técnica Resolvida 1: Auto-avaliação
        if (prestador.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("Você não pode avaliar seu próprio perfil de prestador.");
        }

        // Dívida Técnica Resolvida 2: Spam (Apenas 1 avaliação por pessoa)
        if (avaliacaoRepo.existsByPrestadorAndUsuario(prestador, usuario)) {
            throw new RuntimeException("Você já avaliou este prestador.");
        }

        Avaliacao avaliacaoNova = new Avaliacao();
        avaliacaoNova.setUsuario(usuario);
        avaliacaoNova.setPrestador(prestador);
        avaliacaoNova.setNota(dto.getNota());
        avaliacaoNova.setDescricao(dto.getDescricao());

        Avaliacao salva = avaliacaoRepo.save(avaliacaoNova);

        atualizarMedia(prestador);
        return salva;
    }

    @Transactional
    public void deletarAvaliacao(Long idAvaliacao, String emailLogado) {
        Avaliacao avaliacao = avaliacaoRepo.findById(idAvaliacao)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada."));

        Usuario usuarioLogado = usuarioRepo.findByEmail(emailLogado)
                .orElseThrow(() -> new RuntimeException("Usuário inválido."));

        // Dívida Técnica Resolvida 3: Segurança (Só o dono pode apagar a própria avaliação)
        if (!avaliacao.getUsuario().getId().equals(usuarioLogado.getId())) {
            throw new RuntimeException("Acesso negado: Você não é o autor desta avaliação.");
        }

        PrestadorServico prestador = avaliacao.getPrestador();

        avaliacaoRepo.delete(avaliacao);

        // Força o Hibernate a executar o DELETE no banco antes de re-calcular a média
        avaliacaoRepo.flush();

        atualizarMedia(prestador);
    }

    // Método privado centralizado para recalcular a nota
    private void atualizarMedia(PrestadorServico prestador) {
        Double novaMedia = avaliacaoRepo.calcularMediaDoPrestador(prestador.getId());
        prestador.setMediaAvaliacoes(novaMedia != null ? novaMedia : 0.0);
        prestadorRepo.save(prestador);
    }
}