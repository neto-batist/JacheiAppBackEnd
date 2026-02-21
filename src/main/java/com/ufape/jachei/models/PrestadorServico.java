package com.ufape.jachei.models;

import com.ufape.jachei.models.base.BaseEntity;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "prestadorservicos", indexes = {
        @Index(name = "idx_geo", columnList = "latitude, longitude"),
        @Index(name = "idx_uid", columnList = "firebase_uid")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class PrestadorServico extends BaseEntity {

    @OneToOne(optional = false)
    @JoinColumn(name = "usuario_id", referencedColumnName = "id", nullable = false)
    private Usuario usuario;

    private String cpf;

    @Column(columnDefinition = "TEXT")
    private String descricaoBio;

    // Geolocalização
    private Double latitude;
    private Double longitude;

    // Configurações de Atendimento
    private boolean atende24h;
    private boolean atendeDomiciliar;
    private boolean fazDelivery;

    // Relacionamentos (Mantendo a estrutura do seu banco atual)
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_endereco")
    private Endereco endereco;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id_contato")
    private Contato contato;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "prestadorservicos_has_servicos",
            joinColumns = @JoinColumn(name = "id_prestador_servico"),
            inverseJoinColumns = @JoinColumn(name = "id_servicos")
    )
    private Set<Servico> servicos;

    @Column(columnDefinition = "DOUBLE DEFAULT 0.0")
    private Double mediaAvaliacoes = 0.0;

    @Column(name = "qtd_fotos_servicos", columnDefinition = "INT DEFAULT 0")
    private Integer qtdFotosServicos = 0;

    @OneToMany(mappedBy = "prestador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FotoPortifolio> fotosPortifolio = new ArrayList<>();

    // O serviço destaque (Aparecerá no Card)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servico_principal")
    private Servico servicoPrincipal;
}