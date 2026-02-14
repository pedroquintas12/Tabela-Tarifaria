package com.br.desafio.ras.modules.tabelaTarifaria;

import com.br.desafio.ras.core.exception.ErroResponse;
import com.br.desafio.ras.core.http.ResponseHttpDTO;
import com.br.desafio.ras.modules.tabelaTarifaria.dto.TabelaTarifariaResponseDTO;
import com.br.desafio.ras.modules.tabelaTarifaria.dto.criarTabelaTarifariaDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/tabelas-tarifarias")
@RequiredArgsConstructor
public class TabelaTarifariaResources {

    private final TabelaTarifariaService service;

    private final String logger = this.getClass().getName();

    @Operation(
            summary = "Criação de nova tabela tarifária",
            description = """
            Cria uma nova tabela tarifária contendo categorias e faixas de consumo.

            Regras de negócio:
            - Não pode existir tabela ativa no sistema.
            - A primeira faixa deve iniciar obrigatoriamente em 0.
            - As faixas devem ser contínuas e não podem possuir sobreposição.
            - O valor unitário deve ser maior que zero.
            - Não é permitido criar tabela com vigência expirada.
            """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Tabela tarifária criada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de validação estrutural (JSON inválido ou campos obrigatórios ausentes)",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Violação de regra de negócio",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<ResponseHttpDTO<Void>> criar(
            @Valid
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Objeto para criação da tabela tarifária",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "ExemploCriacaoTabela",
                                    summary = "Exemplo completo de criação",
                                    value = """
                                            {
                                              "nome": "Tabela 2026",
                                              "dataVigenciaInicio": "2026-01-01",
                                              "dataVigenciaFim": "2026-12-31",
                                              "categorias": [
                                                {
                                                  "nome": "COMERCIAL",
                                                  "faixas": [
                                                    {
                                                      "inicio": 0,
                                                      "fim": 10,
                                                      "valorUnitario": 2.5
                                                    },
                                                    {
                                                      "inicio": 11,
                                                      "fim": 20,
                                                      "valorUnitario": 3.0
                                                    }
                                                  ]
                                                }
                                              ]
                                            }
                                            """
                            )
                    )
            )
            @RequestBody criarTabelaTarifariaDTO dto
    ) {
        Logger.getLogger(logger + ".add()")
                .log(Level.INFO, "New request from dto: " + dto);
        service.criarTabela(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseHttpDTO.<Void>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Tabela criada com sucesso!")
                        .build()
        );
    }

    @Operation(
            summary = "Listagem de tabelas tarifárias",
            description = "Retorna todas as tabelas tarifárias ativas (não deletadas)."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista retornada com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(
                                    type = "array",
                                    implementation = TabelaTarifariaResponseDTO.class
                            )
                    )
            )
    })
    @GetMapping
    public ResponseEntity<List<TabelaTarifariaResponseDTO>> listar() {
        return ResponseEntity.ok(service.listar());
    }

    @Operation(
            summary = "Desativa uma tabela tarifária",
            description = "Realiza o soft delete da tabela e de suas categorias/faixas associadas."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Tabela desativada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tabela não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                              "status": 404,
                                              "message": "Tabela não encontrada",
                                              "timestamp": "2026-02-13T17:00:00"
                                            }
                                            """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.desativar(id);
        return ResponseEntity.noContent().build();
    }
}
