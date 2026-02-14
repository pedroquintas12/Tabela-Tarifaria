package com.br.desafio.ras.modules.utils;


import com.br.desafio.ras.modules.faixaConsumo.dto.FaixaInternaDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
public class DetalhamentoDTO {

    private FaixaInternaDTO faixa;
    @Schema(example = "1")
    private Integer m3Cobrados;
    @Schema(example = "13.00")
    private BigDecimal valorUnitario;
    @Schema(example = "13.00")
    private BigDecimal subtotal;

    public DetalhamentoDTO(FaixaInternaDTO faixa,
                           Integer m3Cobrados,
                           BigDecimal valorUnitario,
                           BigDecimal subtotal) {
        this.faixa = faixa;
        this.m3Cobrados = m3Cobrados;
        this.valorUnitario = valorUnitario;
        this.subtotal = subtotal;
    }

}

