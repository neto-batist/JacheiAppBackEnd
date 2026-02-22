package com.ufape.jachei.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrestadorRequest {

    @NotBlank(message = "O CPF é obrigatório")
    private String cpf;

    @NotNull(message = "A latitude é obrigatória")
    private Double latitude;

    @NotNull(message = "A longitude é obrigatória")
    private Double longitude;

    private boolean atende24h;
    private boolean atendeDomiciliar;
    private boolean fazDelivery;
    private String descricaoBio;

    private EnderecoRequest endereco;
    private ContatoRequest contato;

    @Data
    public static class EnderecoRequest {
        private String bairro;
        private String cep;
        private String cidade;
        private String rua;
        private Integer numero;
        private String uf;
    }

    @Data
    public static class ContatoRequest {
        private String telefone;
        private String celular;
        private Byte whatsApp;
        private String email;
    }
}