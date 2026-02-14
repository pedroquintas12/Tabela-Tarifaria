package com.br.desafio.ras.modules.faixaConsumo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
public class FaixaInternaDTO {

    @Schema(example = "0")
    private Integer inicio;
    @Schema(example = "10")
    private Integer fim;

    public FaixaInternaDTO(Integer inicio, Integer fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

}
