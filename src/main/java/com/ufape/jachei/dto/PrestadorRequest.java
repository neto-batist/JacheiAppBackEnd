package com.ufape.jachei.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrestadorRequest {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @Email(message = "Email inválido")
    @NotBlank(message = "Email é obrigatório")
    private String email;

    @NotBlank(message = "O UID do Firebase é obrigatório")
    private String firebaseUid;

    // Coordenadas iniciais
    private Double latitude;
    private Double longitude;

    private boolean atende24h;
    private boolean fazDelivery;
}