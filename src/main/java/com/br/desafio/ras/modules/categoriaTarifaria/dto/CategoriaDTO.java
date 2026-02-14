package com.br.desafio.ras.modules.categoriaTarifaria.dto;


import com.br.desafio.ras.modules.faixaConsumo.dto.FaixaDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class CategoriaDTO {

    @Schema(example = "INDUSTRIAL")
    private String nome;

    private List<FaixaDTO> faixas;

}
