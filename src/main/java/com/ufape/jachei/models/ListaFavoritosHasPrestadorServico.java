package com.ufape.jachei.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "listafavoritos_has_prestadorservicos")
public class ListaFavoritosHasPrestadorServico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prestadorServicos", nullable = false)
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private PrestadorServico idPrestadorservicos;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_contato", nullable = false)
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private ListaFavoritos idListaFavoritos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrestadorServico getIdPrestadorservicos() {
        return idPrestadorservicos;
    }

    public void setIdPrestadorservicos(PrestadorServico idPrestadorservicos) {
        this.idPrestadorservicos = idPrestadorservicos;
    }

    public ListaFavoritos getIdListaFavoritos() {
        return idListaFavoritos;
    }

    public void setIdListaFavoritos(ListaFavoritos idListaFavoritos) {
        this.idListaFavoritos = idListaFavoritos;
    }
}