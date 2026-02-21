package com.ufape.jachei.service;

import com.ufape.jachei.dto.PrestadorRequest;
import com.ufape.jachei.dto.PrestadorSimplesResponse;
import com.ufape.jachei.models.*;
import com.ufape.jachei.repo.PrestadorServicoRepo;
import com.ufape.jachei.repo.ServicoRepo;
import com.ufape.jachei.repo.UsuarioRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class PrestadorService {

    private final PrestadorServicoRepo prestadorRepo;
    private final UsuarioRepo usuarioRepo; // NOVO

    public PrestadorService(PrestadorServicoRepo prestadorRepo, UsuarioRepo usuarioRepo) {
        this.prestadorRepo = prestadorRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Transactional
    public PrestadorServico cadastrarPrestador(PrestadorRequest dto) {
        // 1. Busca o usuário que já existe
        Usuario usuario = usuarioRepo.findByFirebaseUid(dto.getFirebaseUid())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado. Crie a conta antes de virar prestador."));

        PrestadorServico prestador = new PrestadorServico();

        // 2. VINCULA O USUÁRIO AO PRESTADOR (Aqui ele herda Foto, Nome e Email automaticamente!)
        prestador.setUsuario(usuario);

        prestador.setCpf(dto.getCpf());

        //// Dados Geo
        prestador.setLatitude(dto.getLatitude());
        prestador.setLongitude(dto.getLongitude());

        // Flags
        prestador.setAtende24h(dto.isAtende24h());
        prestador.setFazDelivery(dto.isFazDelivery());

        // Mapeando o Endereço que veio da requisição
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

        // Mapeando o Contato que veio da requisição
        Contato contato = new Contato();
        if (dto.getContato() != null) {
            contato.setTelefone(dto.getContato().getTelefone());
            contato.setCelular(dto.getContato().getCelular());
            contato.setWhatsApp(dto.getContato().getWhatsApp() != null ? dto.getContato().getWhatsApp() : (byte) 0);
            contato.setEmail(dto.getContato().getEmail());
            contato.setInstagramLink(dto.getContato().getInstagramLink());
            contato.setFaceBookLink(dto.getContato().getFaceBookLink());
        } else {
            contato.setWhatsApp((byte) 0); // Fallback de segurança para o banco
        }
        prestador.setContato(contato);

        return prestadorRepo.save(prestador);
    }

    public List<PrestadorServico> buscarProximos(double lat, double lng, double raioKm) {
        return prestadorRepo.findNearbyPrestadors(lat, lng, raioKm);
    }

    public Optional<PrestadorServico> buscarPorUid(String uid) {
        return prestadorRepo.findByUsuario_FirebaseUid(uid);
    }

    public Page<PrestadorSimplesResponse> buscarPrestadores(
            Specification<PrestadorServico> spec,
            Pageable pageable,
            String emailLogado) {

        // 1. Busca os prestadores paginados com os filtros
        Page<PrestadorServico> prestadores = prestadorRepo.findAll(spec, pageable);

        // 2. Busca o usuário logado para saber as preferências dele
        Usuario usuario = usuarioRepo.findByEmail(emailLogado).orElse(null);
        Set<PrestadorServico> favoritosDoUser = (usuario != null) ? usuario.getFavoritos() : Set.of();

        // 3. Converte para o DTO Simples setando o booleano
        return prestadores.map(prestador -> {
            boolean isFav = favoritosDoUser.contains(prestador);
            return PrestadorSimplesResponse.fromEntity(prestador, isFav);
        });
    }

    @Autowired // Ou via construtor
    private ServicoRepo servicoRepo;

    @Transactional
    public void adicionarServico(String uidPrestador, Long idServico) {
        PrestadorServico prestador = prestadorRepo.findByUsuario_FirebaseUid(uidPrestador)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        Servico servico = servicoRepo.findById(idServico)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // Adiciona à lista (o Set garante que não duplica)
        prestador.getServicos().add(servico);
        prestadorRepo.save(prestador);
    }

    @Transactional
    public void removerServico(String uidPrestador, Long idServico) {
        PrestadorServico prestador = prestadorRepo.findByUsuario_FirebaseUid(uidPrestador)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        prestador.getServicos().removeIf(s -> s.getId().equals(idServico));
        prestadorRepo.save(prestador);
    }
}