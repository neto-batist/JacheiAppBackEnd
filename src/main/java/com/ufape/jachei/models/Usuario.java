package com.ufape.jachei.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ufape.jachei.models.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuario", indexes = {
        @Index(name = "idx_user_uid", columnList = "firebase_uid")
})
@Data
@EqualsAndHashCode(callSuper = true)
public class Usuario extends BaseEntity implements UserDetails {

    @Column(nullable = false)
    private String nome;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(name = "firebase_uid", unique = true, nullable = false)
    private String firebaseUid;

    @Column(name = "link_foto")
    private String linkFoto;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "usuario_favoritos",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_prestador")
    )
    private Set<PrestadorServico> favoritos;

    @Column(nullable = true) // Nullable porque quem logar com Google futuramente não terá senha
    @JsonIgnore
    private String senha;

    // =======================================================================
    // IMPLEMENTAÇÕES DO USERDETAILS (Obrigatórias para o Spring Security)
    // =======================================================================

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Por padrão, todo mundo tem a role USER. Se fosse um admin, mudaríamos aqui.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return this.senha;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return this.email; // O email será usado como "login"
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() { return true; }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() { return true; }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    @JsonIgnore
    public boolean isEnabled() { return true; }
}