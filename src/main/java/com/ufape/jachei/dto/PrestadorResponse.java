package com.ufape.jachei.dto;

import com.ufape.jachei.models.PrestadorServico;
import lombok.Data;

@Data
public class PrestadorResponse {
    private Long id;
    private String nome;
    private String fotoPerfil;
    private Double latitude;
    private Double longitude;
    private boolean atende24h;
    // Adicione aqui a média de nota se já tiver calculado

    // Construtor estático para converter fácil
    public static PrestadorResponse fromEntity(PrestadorServico entity) {
        PrestadorResponse dto = new PrestadorResponse();
        dto.setId(entity.getId());
        dto.setNome(entity.getUsuario().getNome());
        dto.setFotoPerfil(entity.getUsuario().getLinkFoto());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
        dto.setAtende24h(entity.isAtende24h());
        return dto;
    }
}