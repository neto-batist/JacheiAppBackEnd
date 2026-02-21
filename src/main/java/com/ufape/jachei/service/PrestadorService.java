package com.ufape.jachei.service;

import com.ufape.jachei.dto.PrestadorDetalhadoResponse;
import com.ufape.jachei.dto.PrestadorRequest;
import com.ufape.jachei.dto.PrestadorSearchFilter;
import com.ufape.jachei.dto.PrestadorSimplesResponse;
import com.ufape.jachei.models.*;
import com.ufape.jachei.repo.PrestadorServicoRepo;
import com.ufape.jachei.repo.ServicoRepo;
import com.ufape.jachei.repo.UsuarioRepo;
import com.ufape.jachei.specification.PrestadorSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
public class PrestadorService {

    private final PrestadorServicoRepo prestadorRepo;
    private final UsuarioRepo usuarioRepo;
    private final ServicoRepo servicoRepo; // Movido do @Autowired para Injeção via Construtor

    public PrestadorService(PrestadorServicoRepo prestadorRepo, UsuarioRepo usuarioRepo, ServicoRepo servicoRepo) {
        this.prestadorRepo = prestadorRepo;
        this.usuarioRepo = usuarioRepo;
        this.servicoRepo = servicoRepo;
    }

    private PrestadorServico validarDonoDoPainel(String uid, String emailLogado) {
        PrestadorServico prestador = prestadorRepo.findByUsuario_FirebaseUid(uid)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        if (!prestador.getUsuario().getEmail().equals(emailLogado)) {
            throw new RuntimeException("Acesso Negado: Você não tem permissão para alterar os serviços deste prestador.");
        }
        return prestador;
    }

    @Transactional
    public PrestadorDetalhadoResponse cadastrarPrestador(PrestadorRequest dto, String emailLogado) {

        // 1. Busca quem é o usuário real baseado na assinatura inquebrável do JWT
        Usuario usuario = usuarioRepo.findByEmail(emailLogado)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // 2. Trava de Duplicação
        Optional<PrestadorServico> existente = prestadorRepo.findByUsuario_FirebaseUid(usuario.getFirebaseUid());
        if (existente.isPresent()) {
            throw new RuntimeException("Você já possui um perfil de prestador de serviços.");
        }

        // 3. Cria o perfil ("Upgrade" da conta)
        PrestadorServico prestador = new PrestadorServico();
        prestador.setUsuario(usuario);
        prestador.setCpf(dto.getCpf());
        prestador.setLatitude(dto.getLatitude());
        prestador.setLongitude(dto.getLongitude());
        prestador.setAtende24h(dto.isAtende24h());
        prestador.setAtendeDomiciliar(dto.isAtendeDomiciliar());
        prestador.setFazDelivery(dto.isFazDelivery());

        Endereco endereco = new Endereco();
        if (dto.getEndereco() != null) {
            endereco.setBairro(dto.getEndereco().getBairro());
            endereco.setCep(dto.getEndereco().getCep());
            endereco.setCidade(dto.getEndereco().getCidade());
            endereco.setRua(dto.getEndereco().getRua());
            endereco.setNumero(dto.getEndereco().getNumero());
            endereco.setUf(dto.getEndereco().getUf());
        }
        prestador.setEndereco(endereco);

        Contato contato = new Contato();
        if (dto.getContato() != null) {
            contato.setTelefone(dto.getContato().getTelefone());
            contato.setCelular(dto.getContato().getCelular());
            contato.setWhatsApp(dto.getContato().getWhatsApp() != null ? dto.getContato().getWhatsApp() : (byte) 0);
            contato.setEmail(dto.getContato().getEmail());
        } else {
            contato.setWhatsApp((byte) 0);
        }
        prestador.setContato(contato);

        PrestadorServico salvo = prestadorRepo.save(prestador);

        return PrestadorDetalhadoResponse.fromEntity(salvo, false);
    }

    // =========================================================================
    // NOVO MÉTODO PARA O PAINEL PRIVADO (Usa o email e não o UID)
    // =========================================================================
    public Optional<PrestadorDetalhadoResponse> buscarMeuPainel(String emailLogado) {
        Usuario usuario = usuarioRepo.findByEmail(emailLogado).orElse(null);
        if (usuario == null) return Optional.empty();

        return prestadorRepo.findByUsuario_FirebaseUid(usuario.getFirebaseUid())
                .map(p -> PrestadorDetalhadoResponse.fromEntity(p, false));
    }

    // =========================================================================
    // O CÉREBRO CENTRAL DE BUSCA (A base para a IA)
    // =========================================================================
    @Transactional(readOnly = true)
    public Page<PrestadorSimplesResponse> buscarPrestadores(
            PrestadorSearchFilter filtro,
            Pageable pageable,
            String emailLogado) {

        // 1. Gera a Query Dinâmica baseada nos parâmetros (IA ou Usuário)
        Specification<PrestadorServico> spec = PrestadorSpecification.buildFilter(filtro);

        // 2. Executa a busca paginada no banco
        Page<PrestadorServico> prestadores = prestadorRepo.findAll(spec, pageable);

        // 3. Busca o usuário para cruzar a lista de favoritos
        Usuario usuario = (emailLogado != null) ? usuarioRepo.findByEmail(emailLogado).orElse(null) : null;
        Set<PrestadorServico> favoritosDoUser = (usuario != null) ? usuario.getFavoritos() : Set.of();

        // 4. Mapeia para o Card Enxuto
        return prestadores.map(prestador -> {
            boolean isFav = favoritosDoUser.contains(prestador);
            return PrestadorSimplesResponse.fromEntity(prestador, isFav);
        });
    }

    // =========================================================================
    // BUSCA DETALHADA PARA A TELA DE PERFIL DO PRESTADOR
    // =========================================================================
    @Transactional(readOnly = true)
    public PrestadorDetalhadoResponse buscarDetalhes(Long idPrestador, String emailLogado) {
        PrestadorServico prestador = prestadorRepo.findById(idPrestador)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        Usuario usuario = (emailLogado != null) ? usuarioRepo.findByEmail(emailLogado).orElse(null) : null;
        boolean isFav = (usuario != null) && usuario.getFavoritos().contains(prestador);

        return PrestadorDetalhadoResponse.fromEntity(prestador, isFav);
    }

    public Optional<PrestadorDetalhadoResponse> buscarMeuPerfil(String uid) {
        return prestadorRepo.findByUsuario_FirebaseUid(uid)
                .map(p -> PrestadorDetalhadoResponse.fromEntity(p, false)); // Dono do próprio perfil não é "favorito" de si
    }

    @Transactional
    public void adicionarServico(String uidPrestador, Long idServico, String emailLogado) {
        PrestadorServico prestador = validarDonoDoPainel(uidPrestador, emailLogado); // Protegido!

        Servico servico = servicoRepo.findById(idServico)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        prestador.getServicos().add(servico);
        prestadorRepo.save(prestador);
    }

    @Transactional
    public void removerServico(String uidPrestador, Long idServico, String emailLogado) {
        PrestadorServico prestador = validarDonoDoPainel(uidPrestador, emailLogado); // Protegido!

        prestador.getServicos().removeIf(s -> s.getId().equals(idServico));
        prestadorRepo.save(prestador);
    }
}