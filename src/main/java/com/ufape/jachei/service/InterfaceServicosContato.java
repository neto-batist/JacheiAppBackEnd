package com.ufape.jachei.service;

import com.ufape.jachei.models.Contato;

import java.util.List;

public interface InterfaceServicosContato {
    Contato saveContato(Contato entity);

    Contato findByIdContato(Long id);

    List<Contato> findAllContatos();

    void deleteByIdContato(Long id);

    void deleteContato(Contato entity);
}
