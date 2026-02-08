package com.ufape.jachei.repo;

import com.ufape.jachei.models.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ServicoRepo extends JpaRepository<Servico, Long> {
    List<Servico> findByNomeContainingIgnoreCase(String termo);
    List<Servico> findByCategoriasId(Long idCategoria);
}