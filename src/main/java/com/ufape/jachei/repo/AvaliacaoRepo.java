package com.ufape.jachei.repo;

import com.ufape.jachei.models.Avaliacao;
import com.ufape.jachei.models.PrestadorServico;
import com.ufape.jachei.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepo extends JpaRepository<Avaliacao, Long> {

    // Faz a média matemática super-rápida direto no SQL
    @Query("SELECT COALESCE(AVG(a.nota), 0) FROM Avaliacao a WHERE a.prestador.id = :prestadorId")
    Double calcularMediaDoPrestador(@Param("prestadorId") Long prestadorId);

    // Evita o Spam: Verifica se já existe uma avaliação daquele usuário para aquele prestador
    boolean existsByPrestadorAndUsuario(PrestadorServico prestador, Usuario usuario);
}