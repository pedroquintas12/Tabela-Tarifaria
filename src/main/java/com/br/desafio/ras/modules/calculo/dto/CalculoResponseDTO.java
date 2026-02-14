package com.br.desafio.ras.modules.calculo.dto;


import com.br.desafio.ras.modules.utils.DetalhamentoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculoResponseDTO {

    @Schema(example = "INDUSTRIAL")
    private String categoria;

    @Schema(example = "1")
    private Integer consumoTotal;

    @Schema(example = "13.00")
    private BigDecimal valorTotal;

    private List<DetalhamentoDTO> detalhamento;

}


