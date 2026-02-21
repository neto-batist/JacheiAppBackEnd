package com.ufape.jachei.repo;

import com.ufape.jachei.models.Avaliacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepo extends JpaRepository<Avaliacao, Long> {
    // Busca todas as avaliações de um prestador específico
    List<Avaliacao> findByPrestadorId(Long idPrestador);
    @Query("SELECT COALESCE(AVG(a.nota), 0) FROM Avaliacao a WHERE a.prestador.id = :prestadorId")
    Double calcularMediaDoPrestador(@Param("prestadorId") Long prestadorId);
}