package com.ufape.jachei.specification;

import com.ufape.jachei.models.PrestadorServico;
import org.springframework.data.jpa.domain.Specification;

public class PrestadorSpecification {

    public static Specification<PrestadorServico> temServico(String nomeServico) {
        return (root, query, builder) -> {
            if (nomeServico == null || nomeServico.isEmpty()) return builder.conjunction();
            // Faz um Join na tabela de serviços para filtrar!
            return builder.like(
                    builder.lower(root.join("servicos").get("nome")),
                    "%" + nomeServico.toLowerCase() + "%"
            );
        };
    }

    public static Specification<PrestadorServico> atende24h(Boolean atende) {
        return (root, query, builder) -> {
            if (atende == null) return builder.conjunction();
            return builder.equal(root.get("atende24h"), atende);
        };
    }
}
