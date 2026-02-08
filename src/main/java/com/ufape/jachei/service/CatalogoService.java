package com.ufape.jachei.service;

import com.ufape.jachei.models.Categoria;
import com.ufape.jachei.models.Servico;
import com.ufape.jachei.repo.CategoriaRepo;
import com.ufape.jachei.repo.ServicoRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CatalogoService {

    private final CategoriaRepo categoriaRepo;
    private final ServicoRepo servicoRepo;

    public CatalogoService(CategoriaRepo categoriaRepo, ServicoRepo servicoRepo) {
        this.categoriaRepo = categoriaRepo;
        this.servicoRepo = servicoRepo;
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepo.findAll();
    }

    public List<Servico> listarServicos() {
        return servicoRepo.findAll();
    }

    public List<Servico> buscarServicosPorCategoria(Long idCategoria) {
        return servicoRepo.findByCategoriasId(idCategoria);
    }

    // Métodos para admin (popular o banco inicialmente)
    public Categoria criarCategoria(String nome) {
        Categoria c = new Categoria();
        c.setNome(nome);
        return categoriaRepo.save(c);
    }

    public Servico criarServico(String nome, List<Long> idsCategorias) {
        Servico s = new Servico();
        s.setNome(nome);
        // Aqui você buscaria as categorias pelo ID e setaria no serviço
        // Simplificado para o exemplo
        return servicoRepo.save(s);
    }
}