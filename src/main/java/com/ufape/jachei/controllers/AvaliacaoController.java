package com.ufape.jachei.controllers;

import com.ufape.jachei.models.Avaliacao;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value =  "/jachei/avaliacoes")
@RestController
public class AvaliacaoController {
    @Autowired
    private Facade facede;

    @PostMapping(value = "/salvar-avaliacao")
    public String saveAvaliacao(@RequestBody Avaliacao entity) {
        facede.saveAvaliacao(entity);
        return "Salvo...";
    }

    @GetMapping( value = "/avaliacao/{id}" )
    public Avaliacao findAvaliacao(@PathVariable Long id) {
        return facede.findByIdAvaliacao(id);
    }

    @GetMapping( value = "/avaliacoes-do-prestador/{id}" )
    public List<Avaliacao> findAvaliacaoOfThisPrestador(@PathVariable Long id) {
        return facede.findAllAvaliacaoOfThisPrestador(id);
    }

    @GetMapping( value = "/ver-todas-avaliacoes" )
    public List<Avaliacao> findAllAvaliacoes() {
        return facede.findAllAvaliacoes();
    }

    @PutMapping( value = "/alterar-avaliacao/{id}" )
    public String updateAvaliacao(@PathVariable Long id, @RequestBody @NotNull Avaliacao avaliacao ){
        Avaliacao updateAvaliacao = facede.findByIdAvaliacao(id);

        updateAvaliacao.setDescricao(avaliacao.getDescricao());
        updateAvaliacao.setNota(avaliacao.getNota());

        facede.saveAvaliacao(updateAvaliacao);

        return "Alterado...";
    };

    @DeleteMapping( value = "/remover-avaliacao/{id}")
    public void deleteByIdAvaliacao(@PathVariable Long id) {
        facede.deleteByIdAvaliacao(id);
    }

    public void deleteAvaliacao(Avaliacao entity) {
        facede.deleteAvaliacao(entity);
    }


}
