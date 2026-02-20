package com.ufape.jachei.dto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String token;
    private String firebaseUid;
    private String nome;
    private String linkFoto;
}