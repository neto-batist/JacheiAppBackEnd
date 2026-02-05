package com.ufape.jachei.controllers;

import com.ufape.jachei.models.Contato;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value =  "/jachei/contatos")
@RestController
public class ContatoController {
    @Autowired
    private Facade facede;

    @PostMapping(value = "/salvar-contato")
    public Long saveContato(@RequestBody Contato entity) {
        return facede.saveContato(entity).getId();
    }

    @GetMapping( value = "/contato/{id}" )
    public Contato findContato(@PathVariable Long id) {
        return facede.findByIdContato(id);
    }

    @GetMapping( value = "/ver-todos-contatos" )
    public List<Contato> findAllContatos() {
        return facede.findAllContatos();
    }

    @PutMapping( value = "/alterar-contato/{id}" )
    public String updateContato(@PathVariable Long id, @RequestBody @NotNull Contato contato ){
        Contato updateContato = facede.findByIdContato(id);

        updateContato.setCelular(contato.getCelular());
        updateContato.setWhatsApp(contato.getWhatsApp());
        updateContato.setEmail(contato.getEmail());
        updateContato.setFaceBookLink(contato.getFaceBookLink());
        updateContato.setInstagramLink(contato.getInstagramLink());

        facede.saveContato(updateContato);

        return "Alterado...";
    };

    @DeleteMapping( value = "/remover-contato/{id}")
    public void deleteByIdContato(@PathVariable Long id) {
        facede.deleteByIdContato(id);
    }

    public void deleteContato(Contato entity) {
        facede.deleteContato(entity);
    }


}
