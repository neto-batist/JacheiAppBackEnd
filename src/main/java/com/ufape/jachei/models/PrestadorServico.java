package com.ufape.jachei.models;

import com.ufape.jachei.models.base.BaseEntity;
import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "firebase_uid", unique = true, nullable = false)
    private String firebaseUid;

    private String cpf;

    @Column(name = "foto_perfil")
    private String fotoPerfil;

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
}