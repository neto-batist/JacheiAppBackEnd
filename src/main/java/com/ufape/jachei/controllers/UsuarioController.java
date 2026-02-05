package com.ufape.jachei.controllers;

import com.ufape.jachei.models.Usuario;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(value =  "/jachei/usuarios")
@RestController
public class UsuarioController {
    @Autowired
    private Facade facede;

    @PostMapping(value = "/salvar-usuario")
    public String saveUsuario(@RequestBody Usuario entity) {
        facede.saveUsuario(entity);
        return "Salvo...";
    }

    @GetMapping( value = "/usuario/{id}" )
    public Usuario findUsuario(@PathVariable Long id) {
        return facede.findByIdUsuario(id);
    }

    @GetMapping( value = "/ver-todos-usuarios" )
    public List<Usuario> findAllUsuarios() {
        return facede.findAllUsuarios();
    }

    @PutMapping( value = "/alterar-usurario/{id}" )
    public String updateUser(@PathVariable Long id, @RequestBody @NotNull Usuario usuario ){
        Usuario updateUser = facede.findByIdUsuario(id);

        updateUser.setLinkFoto(usuario.getLinkFoto());
        updateUser.setNome(usuario.getNome());

        facede.saveUsuario(updateUser);

        return "Alterado...";
    };

    @DeleteMapping( value = "/remover-usuario/{id}")
    public void deleteByIdUsuario(@PathVariable Long id) {
        facede.deleteByIdUsuario(id);
    }

    public void deleteUsuario(Usuario entity) {
        facede.deleteUsuario(entity);
    }


}
