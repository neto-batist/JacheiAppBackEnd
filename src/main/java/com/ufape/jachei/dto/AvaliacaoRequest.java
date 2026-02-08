package com.ufape.jachei.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AvaliacaoRequest {
    @NotNull(message = "ID do prestador é obrigatório")
    private Long idPrestador;

    @NotBlank(message = "UID do usuário é obrigatório")
    private String uidUsuario; // Quem está avaliando

    @Min(1) @Max(5)
    private Integer nota;

    private String descricao;
}