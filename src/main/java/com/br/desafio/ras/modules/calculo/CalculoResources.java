package com.br.desafio.ras.modules.calculo;

import com.br.desafio.ras.core.exception.ErroResponse;
import com.br.desafio.ras.modules.calculo.dto.CalculoRequestDTO;
import com.br.desafio.ras.modules.calculo.dto.CalculoResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/calculos")
@RequiredArgsConstructor
public class CalculoResources {

    private final CalculoService service;
    private final String logger = this.getClass().getName();

    @Operation(summary = "Realiza o cálculo da tarifa")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Cálculo realizado com sucesso",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CalculoResponseDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Erro de regra de negócio",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErroResponse.class),
                            examples = {
                                    @ExampleObject(
                                            name = "Tabela não encontrada",
                                            value = """
                                                {
                                                  "status": 404,
                                                  "message": "Nenhuma tabela ativa encontrada",
                                                  "timestamp": "2026-02-13T17:00:00"
                                                }
                                                """
                                    ),
                                    @ExampleObject(
                                            name = "Categoria não encontrada",
                                            value = """
                                                {
                                                  "status": 404,
                                                  "message": "Categoria não encontrada",
                                                  "timestamp": "2026-02-13T17:05:00"
                                                }
                                                """),
                                    @ExampleObject(
                                            name = "Consumo menor ou igual 0",
                                            value = """
                                                {
                                                  "status": 409,
                                                  "message": "O consumo deve ser maior que 0",
                                                  "timestamp": "2026-02-13T17:05:00"
                                                }
                                                """
                                    )
                            }
                    )



            )
    })
    @PostMapping
    public ResponseEntity<CalculoResponseDTO> calcular(
            @RequestBody CalculoRequestDTO request) {
        Logger.getLogger(logger + ".add()")
                .log(Level.INFO, "New request from dto: " + request.toString());
        return ResponseEntity.ok(service.calcular(request));
    }
}

