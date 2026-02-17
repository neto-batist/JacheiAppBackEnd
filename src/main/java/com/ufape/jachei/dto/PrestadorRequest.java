package com.ufape.jachei.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PrestadorRequest {

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @NotBlank(message = "O UID do Firebase é obrigatório")
    private String firebaseUid;

    // Coordenadas iniciais
    private Double latitude;
    private Double longitude;

    private boolean atende24h;
    private boolean fazDelivery;

    // ---> NOVOS CAMPOS ANINHADOS <---
    private EnderecoRequest endereco;
    private ContatoRequest contato;

    @Data
    public static class EnderecoRequest {
        private String bairro;
        private String cep;
        private String cidade;
        private String rua;
        private int numero;
        private String uf;
    }

    @Data
    public static class ContatoRequest {
        private String telefone;
        private String celular;
        private Byte whatsApp; // Usa Byte pois seu banco espera 1 ou 0
        private String email;
        private String instagramLink;
        private String faceBookLink;
    }
}