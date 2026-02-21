package com.ufape.jachei.dto;
import com.ufape.jachei.models.PrestadorServico;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrestadorSimplesResponse {
    private Long id;
    private String nome;
    private String fotoPerfil;
    private boolean atende24H;
    private boolean fazDelivery;
    private String servicoPrincipal;
    private Double mediaAvaliacoes;
    private boolean isFavorito; // Calculado dinamicamente no Service!

    public static PrestadorSimplesResponse fromEntity(PrestadorServico p, boolean favorito) {
        String servicoStr = (p.getServicoPrincipal() != null) ? p.getServicoPrincipal().getNome() : "Serviços Gerais";

        return PrestadorSimplesResponse.builder()
                .id(p.getId())
                .nome(p.getUsuario().getNome())
                .fotoPerfil(p.getUsuario().getLinkFoto())
                .atende24H(p.isAtende24h())
                .fazDelivery(p.isFazDelivery())
                .servicoPrincipal(servicoStr)
                .mediaAvaliacoes(p.getMediaAvaliacoes())
                .isFavorito(favorito)
                .build();
    }
}