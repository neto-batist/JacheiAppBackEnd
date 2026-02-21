package com.ufape.jachei.specification;

import com.ufape.jachei.dto.PrestadorSearchFilter;
import com.ufape.jachei.models.PrestadorServico;
import com.ufape.jachei.utils.GeoUtils;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

public class PrestadorSpecification {

    public static Specification<PrestadorServico> buildFilter(PrestadorSearchFilter filtro) {
        return (root, query, builder) -> {

            // Lista que guardará todos os filtros dinâmicos
            List<Predicate> predicates = new ArrayList<>();

            // 1. Filtrar pelo nome do Prestador (dentro da tabela Usuario)
            if (filtro.getNomePrestador() != null && !filtro.getNomePrestador().trim().isEmpty()) {
                String busca = "%" + filtro.getNomePrestador().toLowerCase() + "%";
                predicates.add(builder.like(builder.lower(root.join("usuario").get("nome")), busca));
            }

            // 2. Filtrar pelo nome de um Serviço oferecido
            if (filtro.getNomeServico() != null && !filtro.getNomeServico().trim().isEmpty()) {
                String busca = "%" + filtro.getNomeServico().toLowerCase() + "%";
                // Faz o Join da tabela intermediária até a tabela de Serviços para buscar
                predicates.add(builder.like(builder.lower(root.join("servicos").get("nome")), busca));
            }

            // 3. Filtrar por Cidade
            if (filtro.getCidade() != null && !filtro.getCidade().trim().isEmpty()) {
                predicates.add(builder.equal(builder.lower(root.join("endereco").get("cidade")), filtro.getCidade().toLowerCase()));
            }

            // 4. Filtros Booleanos
            if (filtro.getAtende24h() != null) {
                predicates.add(builder.equal(root.get("atende24h"), filtro.getAtende24h()));
            }
            if (filtro.getAtendeDomiciliar() != null) {
                predicates.add(builder.equal(root.get("atendeDomiciliar"), filtro.getAtendeDomiciliar()));
            }
            if (filtro.getFazDelivery() != null) {
                predicates.add(builder.equal(root.get("fazDelivery"), filtro.getFazDelivery()));
            }

            // 5. Nota Mínima (Excelente para o Algoritmo de ML!)
            if (filtro.getNotaMinima() != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("mediaAvaliacoes"), filtro.getNotaMinima()));
            }

            // 6. Buscador Inteligente por Proximidade (Bounding Box)
            if (filtro.getLatitudeUsuario() != null && filtro.getLongitudeUsuario() != null && filtro.getRaioKm() != null) {
                double[] box = GeoUtils.getBoundingBox(filtro.getLatitudeUsuario(), filtro.getLongitudeUsuario(), filtro.getRaioKm());

                // Limites de Latitude
                predicates.add(builder.between(root.get("latitude"), box[0], box[1]));
                // Limites de Longitude
                predicates.add(builder.between(root.get("longitude"), box[2], box[3]));
            }

            // Garante que o Join de serviços não retorne o mesmo prestador duplicado na mesma página
            query.distinct(true);

            // Junta todas as regras com "AND" e entrega o pacote pro Banco de Dados executar!
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}