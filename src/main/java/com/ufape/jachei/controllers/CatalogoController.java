package com.ufape.jachei.controllers;

import com.ufape.jachei.models.Categoria;
import com.ufape.jachei.models.Servico;
import com.ufape.jachei.service.CatalogoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalogo")
public class CatalogoController {

    private final CatalogoService catalogoService;

    public CatalogoController(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }

    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> listarCategorias() {
        return ResponseEntity.ok(catalogoService.listarCategorias());
    }

    @GetMapping("/servicos")
    public ResponseEntity<List<Servico>> listarServicos(@RequestParam(required = false) Long categoriaId) {
        if (categoriaId != null) {
            return ResponseEntity.ok(catalogoService.buscarServicosPorCategoria(categoriaId));
        }
        return ResponseEntity.ok(catalogoService.listarServicos());
    }
}