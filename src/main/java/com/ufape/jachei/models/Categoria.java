package com.ufape.jachei.models;

import com.ufape.jachei.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "categorias")
@Data
@EqualsAndHashCode(callSuper = true)
public class Categoria extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String nome;
}