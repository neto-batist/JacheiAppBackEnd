package com.ufape.jachei.controllers;


import com.ufape.jachei.models.PrestadorServicosHasServicos;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value =  "/jachei/servicos/prestador-de-servicos")
@RestController
public class PrestadorServicosHasServicosController {
    @Autowired
    private Facade facede;

    @PostMapping(value = "/adicionar-servico-prestador-servico")
    public Long savePrestadorServicosHasServicos(@RequestBody PrestadorServicosHasServicos entity) {
        return facede.savePrestadorServicosHasServicos(entity).getId();
    }

    @GetMapping( value = "/ver-todos-servicos-prestador" )
    public List<PrestadorServicosHasServicos> findAllPrestadorServicosHasServicos() {
        return facede.findAllPrestadorServicosHasServicos();
    }

    @PutMapping( value = "/alterar-servicos-prestador-servicos/{id}" )
    public String updatePrestadorServicosHasServicos(@PathVariable Long id, @RequestBody @NotNull PrestadorServicosHasServicos prestadorServicosHasServicos ){
        PrestadorServicosHasServicos updatePrestadorServicosHasServicos = facede.findByIdPrestadorServicosHasServicos(id);

        updatePrestadorServicosHasServicos.setIdServicos(prestadorServicosHasServicos.getIdServicos());
        updatePrestadorServicosHasServicos.setIdPrestadorServico(prestadorServicosHasServicos.getIdPrestadorServico());

        facede.savePrestadorServicosHasServicos(updatePrestadorServicosHasServicos);

        return "Alterado...";
    };

    @DeleteMapping( value = "/remover-servico-prestador-servico/{id}")
    public void deleteByIdPrestadorServicosHasServicos(@PathVariable Long id) {
        facede.deleteByIdPrestadorServicosHasServicos(id);
    }

    public void deletePrestadorServicosHasServicos(PrestadorServicosHasServicos entity) {
        facede.deletePrestadorServicosHasServicos(entity);
    }


}
