package com.br.desafio.ras.modules.faixaConsumo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class FaixaDTO {

    @Schema(example = "0")
    private Integer inicio;
    @Schema(example = "10")
    private Integer fim;

    @Schema(example = "13")
    private BigDecimal valorUnitario;

}
