package com.br.desafio.ras.modules.tabelaTarifaria.dto;

import com.br.desafio.ras.modules.categoriaTarifaria.dto.CategoriaDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDate;
import java.util.Set;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class criarTabelaTarifariaDTO {

    @Schema(example = "Tabela 2026")
    private String nome;
    @Schema(example = "2026-02-01")
    private LocalDate dataVigenciaInicio;
    @Schema(example = "2026-02-31")
    private LocalDate dataVigenciaFim;

    private Set<CategoriaDTO> categorias;

}
