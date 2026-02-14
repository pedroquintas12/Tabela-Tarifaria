package com.br.desafio.ras.modules.calculo.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CalculoRequestDTO {
    @Schema(example = "INDUSTRIAL")
    private String categoria;
    @Schema(example = "1")
    private Integer consumo;

}

