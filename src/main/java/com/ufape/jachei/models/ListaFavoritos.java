package com.ufape.jachei.models;

import jakarta.persistence.*;

@Entity
@Table(name = "listafavoritos")
public class ListaFavoritos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    //TODO [JPA Buddy] generate columns from DB
}