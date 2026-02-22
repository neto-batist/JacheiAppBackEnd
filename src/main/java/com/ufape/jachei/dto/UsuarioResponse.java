package com.ufape.jachei.dto;

import com.ufape.jachei.models.Usuario;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UsuarioResponse {
    private String firebaseUid;
    private String nome;
    private String email;
    private String linkFoto;

    public static UsuarioResponse fromEntity(Usuario u) {
        return UsuarioResponse.builder()
                .firebaseUid(u.getFirebaseUid())
                .nome(u.getNome())
                .email(u.getEmail())
                .linkFoto(u.getLinkFoto())
                .build();
    }
}