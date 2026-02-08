package com.ufape.jachei.service;


import com.ufape.jachei.models.Categoria;
import com.ufape.jachei.repo.CategoriaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicosCategoria implements InterfaceServicosCategoria {

    @Autowired
    private CategoriaRepo repositorioCategoria;

    @Override
    public Categoria saveCategoria(Categoria entity) {

        return repositorioCategoria.save(entity);
    }

    @Override
    public Categoria findByIdCategoria(Long id) {
        Optional<Categoria> Categoria = repositorioCategoria.findById(id);
        if(Categoria.isPresent()){
            return Categoria.get();
        }else{
            return null;
        }
    }

    @Override
    public List<Categoria> findAllCategorias() {
        return repositorioCategoria.findAll();
    }

    @Override
    public void deleteByIdCategoria(Long id) {
        repositorioCategoria.deleteById(id);
    }

    @Override
    public void deleteCategoria(Categoria entity) {
        repositorioCategoria.delete(entity);
    }
}
