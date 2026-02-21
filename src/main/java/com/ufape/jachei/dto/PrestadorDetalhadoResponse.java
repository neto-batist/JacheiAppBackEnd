package com.ufape.jachei.dto;

import com.ufape.jachei.models.PrestadorServico;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class PrestadorDetalhadoResponse {

    // --- DADOS PRINCIPAIS ---
    private Long id;
    private String firebaseUid;
    private String nome;
    private String fotoPerfil;
    private String cpf;
    private String descricaoBio;

    // --- GEOLOCALIZAÇÃO ---
    private Double latitude;
    private Double longitude;

    // --- CONFIGURAÇÕES DE ATENDIMENTO ---
    private boolean atende24h;
    private boolean atendeDomiciliar;
    private boolean fazDelivery;

    // --- AVALIAÇÕES E PORTFÓLIO ---
    private Double mediaAvaliacoes;
    private Integer qtdFotosServicos;

    // --- STATUS DINÂMICO ---
    private boolean isFavorito; // Propriedade de contexto do usuário logado

    // --- RELACIONAMENTOS (Objetos Mastigados) ---
    private ServicoDTO servicoPrincipal;
    private List<ServicoDTO> servicos;
    private EnderecoDTO endereco;
    private ContatoDTO contato;
    private List<FotoPortifolioDTO> portifolio;

    // ========================================================================
    // INNER CLASSES (Representações seguras e limpas das tabelas conectadas)
    // ========================================================================

    @Data
    @Builder
    public static class ServicoDTO {
        private Long id;
        private String nome;
    }

    @Data
    @Builder
    public static class EnderecoDTO {
        private String cep;
        private String rua;
        private Integer numero;
        private String bairro;
        private String cidade;
        private String uf;
    }

    @Data
    @Builder
    public static class ContatoDTO {
        private String telefone;
        private String celular;
        private String email;
        private Byte whatsApp;
    }

    @Data
    @Builder
    public static class FotoPortifolioDTO {
        private Long id;
        private String urlFoto;
    }

    // ========================================================================
    // FÁBRICA DE CONVERSÃO (Entity -> DTO) COM NULL SAFETY
    // ========================================================================

    public static PrestadorDetalhadoResponse fromEntity(PrestadorServico p, boolean isFavorito) {

        // 1. Mapeamento Seguro do Serviço Principal
        ServicoDTO servicoPrincipalDto = p.getServicoPrincipal() != null
                ? ServicoDTO.builder()
                .id(p.getServicoPrincipal().getId())
                .nome(p.getServicoPrincipal().getNome())
                .build()
                : null;

        // 2. Mapeamento Seguro da Lista de Serviços
        List<ServicoDTO> servicosDto = p.getServicos() != null
                ? p.getServicos().stream()
                .map(s -> ServicoDTO.builder().id(s.getId()).nome(s.getNome()).build())
                .collect(Collectors.toList())
                : List.of();

        // 3. Mapeamento Seguro do Endereço
        EnderecoDTO enderecoDto = p.getEndereco() != null
                ? EnderecoDTO.builder()
                .cep(p.getEndereco().getCep())
                .rua(p.getEndereco().getRua())
                .numero(p.getEndereco().getNumero())
                .bairro(p.getEndereco().getBairro())
                .cidade(p.getEndereco().getCidade())
                .uf(p.getEndereco().getUf())
                .build()
                : null;

        // 4. Mapeamento Seguro do Contato
        ContatoDTO contatoDto = p.getContato() != null
                ? ContatoDTO.builder()
                .telefone(p.getContato().getTelefone())
                .celular(p.getContato().getCelular())
                .email(p.getContato().getEmail())
                .whatsApp(p.getContato().getWhatsApp())
                .build()
                : null;

        List<FotoPortifolioDTO> fotosDto = p.getFotosPortifolio() != null
                ? p.getFotosPortifolio().stream()
                .map(f -> FotoPortifolioDTO.builder().id(f.getId()).urlFoto(f.getUrlFoto()).build())
                .collect(Collectors.toList())
                : List.of();

        // 5. Construção Final do Objeto Master
        return PrestadorDetalhadoResponse.builder()
                .id(p.getId())
                .firebaseUid(p.getUsuario().getFirebaseUid())
                .nome(p.getUsuario().getNome())
                .fotoPerfil(p.getUsuario().getLinkFoto())
                .cpf(p.getCpf())
                .descricaoBio(p.getDescricaoBio())
                .latitude(p.getLatitude())
                .longitude(p.getLongitude())
                .atende24h(p.isAtende24h())
                .atendeDomiciliar(p.isAtendeDomiciliar())
                .fazDelivery(p.isFazDelivery())
                .mediaAvaliacoes(p.getMediaAvaliacoes())
                .qtdFotosServicos(p.getQtdFotosServicos())
                .portifolio(fotosDto)
                .servicoPrincipal(servicoPrincipalDto)
                .servicos(servicosDto)
                .endereco(enderecoDto)
                .contato(contatoDto)
                .isFavorito(isFavorito)
                .build();
    }
}