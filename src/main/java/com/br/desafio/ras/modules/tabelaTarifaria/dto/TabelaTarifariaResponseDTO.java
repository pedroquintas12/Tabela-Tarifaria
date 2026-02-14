package com.br.desafio.ras.modules.tabelaTarifaria.dto;


import com.br.desafio.ras.modules.categoriaTarifaria.dto.CategoriaResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
public class TabelaTarifariaResponseDTO {

    @Schema(example = "1")
    private Long id;
    @Schema(example = "Tabela 2026")
    private String nome;
    @Schema(example = "2026-02-01")
    private LocalDate dataVigenciaInicio;
    @Schema(example = "2026-02-31")
    private LocalDate dataVigenciaFim;

    private List<CategoriaResponseDTO> categorias;

    public TabelaTarifariaResponseDTO(Long id,
                                      String nome,
                                      LocalDate dataVigenciaInicio,
                                      LocalDate dataVigenciaFim,
                                      List<CategoriaResponseDTO> categorias) {
        this.id = id;
        this.nome = nome;
        this.dataVigenciaInicio = dataVigenciaInicio;
        this.dataVigenciaFim = dataVigenciaFim;
        this.categorias = categorias;
    }

}

