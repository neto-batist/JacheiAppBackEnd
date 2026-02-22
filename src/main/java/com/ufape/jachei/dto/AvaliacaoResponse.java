package com.ufape.jachei.dto;

import com.ufape.jachei.models.Avaliacao;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AvaliacaoResponse {
    private Long id;
    private Integer nota;
    private String descricao;
    private String nomeAvaliador;
    private String fotoAvaliador;

    public static AvaliacaoResponse fromEntity(Avaliacao a) {
        return AvaliacaoResponse.builder()
                .id(a.getId())
                .nota(a.getNota())
                .descricao(a.getDescricao())
                .nomeAvaliador(a.getUsuario().getNome())
                .fotoAvaliador(a.getUsuario().getLinkFoto())
                .build();
    }
}