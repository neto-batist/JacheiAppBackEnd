package com.ufape.jachei.service;

import com.ufape.jachei.dto.PrestadorRequest;
import com.ufape.jachei.models.PrestadorServico;
import com.ufape.jachei.models.Servico;
import com.ufape.jachei.repo.PrestadorServicoRepo;
import com.ufape.jachei.repo.ServicoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class PrestadorService {

    private final PrestadorServicoRepo prestadorRepo;

    public PrestadorService(PrestadorServicoRepo prestadorRepo) {
        this.prestadorRepo = prestadorRepo;
    }

    @Transactional
    public PrestadorServico cadastrarPrestador(PrestadorRequest dto) {
        // Futuramente: Validar se CPF ou Email já existem antes de salvar

        PrestadorServico prestador = new PrestadorServico();
        prestador.setNome(dto.getNome());
        prestador.setCpf(dto.getCpf());
        prestador.setEmail(dto.getEmail());
        prestador.setFirebaseUid(dto.getFirebaseUid());

        // Dados Geo
        prestador.setLatitude(dto.getLatitude());
        prestador.setLongitude(dto.getLongitude());

        // Flags
        prestador.setAtende24h(dto.isAtende24h());
        prestador.setFazDelivery(dto.isFazDelivery());

        return prestadorRepo.save(prestador);
    }

    public List<PrestadorServico> buscarProximos(double lat, double lng, double raioKm) {
        return prestadorRepo.findNearbyPrestadors(lat, lng, raioKm);
    }

    public Optional<PrestadorServico> buscarPorUid(String uid) {
        return prestadorRepo.findByFirebaseUid(uid);
    }

    @Autowired // Ou via construtor
    private ServicoRepo servicoRepo;

    @Transactional
    public void adicionarServico(String uidPrestador, Long idServico) {
        PrestadorServico prestador = prestadorRepo.findByFirebaseUid(uidPrestador)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        Servico servico = servicoRepo.findById(idServico)
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        // Adiciona à lista (o Set garante que não duplica)
        prestador.getServicos().add(servico);
        prestadorRepo.save(prestador);
    }

    @Transactional
    public void removerServico(String uidPrestador, Long idServico) {
        PrestadorServico prestador = prestadorRepo.findByFirebaseUid(uidPrestador)
                .orElseThrow(() -> new RuntimeException("Prestador não encontrado"));

        prestador.getServicos().removeIf(s -> s.getId().equals(idServico));
        prestadorRepo.save(prestador);
    }
}