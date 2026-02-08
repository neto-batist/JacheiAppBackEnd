package com.ufape.jachei.repo;

import com.ufape.jachei.models.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepo extends JpaRepository<Avaliacao, Long> {
    // Busca todas as avaliações de um prestador específico
    List<Avaliacao> findByPrestadorId(Long idPrestador);
}