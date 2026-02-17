package com.ufape.jachei.config;

import com.ufape.jachei.models.Categoria;
import com.ufape.jachei.models.Servico;
import com.ufape.jachei.repo.CategoriaRepo;
import com.ufape.jachei.repo.ServicoRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CategoriaRepo categoriaRepo;
    private final ServicoRepo servicoRepo;

    public DataSeeder(CategoriaRepo categoriaRepo, ServicoRepo servicoRepo) {
        this.categoriaRepo = categoriaRepo;
        this.servicoRepo = servicoRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // Só popula se o banco estiver vazio
        if (categoriaRepo.count() == 0) {

            // 1. Criando Categorias baseadas no seu escopo
            Categoria eletrica = salvarCategoria("Elétrica");
            Categoria hidraulica = salvarCategoria("Hidráulica");
            Categoria limpeza = salvarCategoria("Limpeza");
            Categoria transporte = salvarCategoria("Transporte");
            Categoria beleza = salvarCategoria("Beleza");
            Categoria ti = salvarCategoria("TI");

            // 2. Criando Serviços base e atrelando às categorias
            salvarServico("Instalação de Tomadas", eletrica);
            salvarServico("Reparo de Fiação", eletrica);
            salvarServico("Conserto de Infiltração", hidraulica);
            salvarServico("Diarista Completa", limpeza);
            salvarServico("Limpeza Pós-Obra", limpeza);
            salvarServico("Frete de Mudança", transporte);
            salvarServico("Corte de Cabelo Domiciliar", beleza);
            salvarServico("Manicure", beleza);
            salvarServico("Formatação de Computador", ti);
            salvarServico("Criação de Sites", ti);

            System.out.println("✅ Categorias e Serviços pré-estabelecidos com sucesso!");
        }
    }

    private Categoria salvarCategoria(String nome) {
        Categoria cat = new Categoria();
        cat.setNome(nome);
        return categoriaRepo.save(cat);
    }

    private void salvarServico(String nome, Categoria categoria) {
        Servico servico = new Servico();
        servico.setNome(nome);
        servico.setCategorias(Set.of(categoria)); // Relacionamento Muitos para Muitos
        servicoRepo.save(servico);
    }
}