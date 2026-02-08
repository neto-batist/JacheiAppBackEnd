package com.ufape.jachei.service;


import com.ufape.jachei.models.Servico;
import com.ufape.jachei.repo.ServicoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServicosServico implements InterfaceServicosServico {

    @Autowired
    private ServicoRepo repositorioServico;


    @Override
    public Servico saveServico(Servico entity) {

        return repositorioServico.save(entity);
    }

    @Override
    public Servico findByIdServico(Long id) {
        Optional<Servico> Servico = repositorioServico.findById(id);
        if(Servico.isPresent()){
            return Servico.get();
        }else{
            return null;
        }
    }


    @Override
    public List<Servico> findAllServicos() {
        return repositorioServico.findAll();
    }


    @Override
    public void deleteByIdServico(Long id) {
        repositorioServico.deleteById(id);
    }


    @Override
    public void deleteServico(Servico entity) {
        repositorioServico.delete(entity);
    }
}
