package com.ufape.jachei.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
@Table(name = "prestadorservicos_has_servicos")
public class PrestadorServicosHasServicos {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_servicos", nullable = false)
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private Servico idServicos;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_prestador_servico", nullable = false)
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private PrestadorServico idPrestadorServico;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Servico getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(Servico idServicos) {
        this.idServicos = idServicos;
    }

    public PrestadorServico getIdPrestadorServico() {
        return idPrestadorServico;
    }

    public void setIdPrestadorServico(PrestadorServico idPrestadorServico) {
        this.idPrestadorServico = idPrestadorServico;
    }
}