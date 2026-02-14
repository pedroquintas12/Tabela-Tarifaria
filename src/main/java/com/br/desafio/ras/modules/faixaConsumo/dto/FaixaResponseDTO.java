package com.br.desafio.ras.modules.faixaConsumo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;


@Getter
@Setter
@Builder
@NoArgsConstructor
public class FaixaResponseDTO {

    @Schema(example = "0")
    private Integer inicio;

    @Schema(example = "10")
    private Integer fim;

    @Schema(example = "13.00")
    private BigDecimal valorUnitario;

    public FaixaResponseDTO(Integer inicio,
                            Integer fim,
                            BigDecimal valorUnitario) {
        this.inicio = inicio;
        this.fim = fim;
        this.valorUnitario = valorUnitario;
    }

}
