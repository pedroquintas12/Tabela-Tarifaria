package com.br.desafio.ras.modules.categoriaTarifaria.dto;


import com.br.desafio.ras.modules.faixaConsumo.dto.FaixaResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class CategoriaResponseDTO {

    @Schema(example = "INDUSTRIAL")
    private String nome;

    private List<FaixaResponseDTO> faixas;

    public CategoriaResponseDTO(String nome,
                                List<FaixaResponseDTO> faixas) {
        this.nome = nome;
        this.faixas = faixas;
    }

}

