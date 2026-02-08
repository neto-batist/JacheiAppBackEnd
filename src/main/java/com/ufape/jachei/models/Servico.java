package com.ufape.jachei.models;

import com.ufape.jachei.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

@Entity
@Table(name = "servicos")
@Data
@EqualsAndHashCode(callSuper = true)
public class Servico extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String nome;

    // Um serviço pode ter várias categorias (Ex: "Limpeza de Piscina" -> "Limpeza", "Casa")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "servicos_has_categorias",
            joinColumns = @JoinColumn(name = "id_servicos"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private Set<Categoria> categorias;
}