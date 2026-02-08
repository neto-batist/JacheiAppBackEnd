package com.ufape.jachei.models;

import com.ufape.jachei.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Set;

@Entity
@Table(name = "usuario", indexes = {
        @Index(name = "idx_user_uid", columnList = "firebase_uid")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class Usuario extends BaseEntity {

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    // Vínculo de segurança com o Firebase
    @Column(name = "firebase_uid", unique = true, nullable = false)
    private String firebaseUid;

    @Column(name = "link_foto")
    private String linkFoto;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_favoritos", // Nome da tabela no banco (substitui ListaFavoritosHas...)
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_prestador")
    )
    private Set<PrestadorServico> favoritos;
}