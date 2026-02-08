package com.ufape.jachei.service;

import com.ufape.jachei.models.Categoria;

import java.util.List;

public interface InterfaceServicosCategoria {
    Categoria saveCategoria(Categoria entity);

    Categoria findByIdCategoria(Long id);

    List<Categoria> findAllCategorias();

    void deleteByIdCategoria(Long id);

    void deleteCategoria(Categoria entity);
}
