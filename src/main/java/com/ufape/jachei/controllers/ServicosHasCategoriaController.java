package com.ufape.jachei.controllers;


import com.ufape.jachei.models.ServicosHasCategoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RequestMapping(value =  "/jachei/servicos")
@RestController
public class ServicosHasCategoriaController {
    @Autowired
    //private Facade facede;


    @GetMapping( value = "/ver-todos-servicos" )
    public List<ServicosHasCategoria> findAllServicosHasCategoria() {
        return facede.findAllServicosHasCategoria();
    }

}
