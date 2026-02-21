package com.ufape.jachei.dto;

import lombok.Data;

@Data
public class PrestadorSearchFilter {
    private String nomePrestador; // Busca no nome do Usuário
    private String nomeServico;   // Ex: "Eletricista"
    private String cidade;        // Ex: "Garanhuns"

    private Boolean atende24h;
    private Boolean atendeDomiciliar;
    private Boolean fazDelivery;

    private Double notaMinima;    // Ex: IA só quer recomendar quem tem >= 4.0

    // --- Dados para Busca Geoespacial ---
    private Double latitudeUsuario;
    private Double longitudeUsuario;
    private Double raioKm;        // Ex: Buscar em até 15km de distância
}