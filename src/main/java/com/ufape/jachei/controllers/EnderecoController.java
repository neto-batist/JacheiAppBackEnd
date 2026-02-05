package com.ufape.jachei.controllers;

import com.ufape.jachei.models.Endereco;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value =  "/jachei/enderecos")
@RestController
public class EnderecoController {
    @Autowired
    private Facade facede;

    @PostMapping(value = "/salvar-endereco")
    public Long saveEndereco(@RequestBody Endereco entity) {
        return facede.saveEndereco(entity).getId();
    }

    @GetMapping( value = "/endereco/{id}" )
    public Endereco findEndereco(@PathVariable Long id) {
        return facede.findByIdEndereco(id);
    }

    @GetMapping( value = "/ver-todos-enderecos" )
    public List<Endereco> findAllEnderecos() {
        return facede.findAllEnderecos();
    }

    @PutMapping( value = "/alterar-endereco/{id}" )
    public String updateEndereco(@PathVariable Long id, @RequestBody @NotNull Endereco endereco ){
        Endereco updateEndereco = facede.findByIdEndereco(id);

        updateEndereco.setUf(endereco.getUf());
        updateEndereco.setCep(endereco.getCep());
        updateEndereco.setCidade(endereco.getCidade());
        updateEndereco.setBairro(endereco.getBairro());
        updateEndereco.setRua(endereco.getRua());
        updateEndereco.setNumero(endereco.getNumero());


        facede.saveEndereco(updateEndereco);

        return "Alterado...";
    };

    @DeleteMapping( value = "/remover-endereco/{id}")
    public void deleteByIdEndereco(@PathVariable Long id) {
        facede.deleteByIdEndereco(id);
    }

    public void deleteEndereco(Endereco entity) {
        facede.deleteEndereco(entity);
    }


}
