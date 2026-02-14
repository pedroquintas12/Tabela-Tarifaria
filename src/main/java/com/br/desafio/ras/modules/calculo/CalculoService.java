package com.br.desafio.ras.modules.calculo;


import com.br.desafio.ras.core.exception.BusinessRuleException;
import com.br.desafio.ras.core.exception.RecursoNaoEncontradoException;
import com.br.desafio.ras.modules.categoriaTarifaria.CategoriaTarifaria;
import com.br.desafio.ras.modules.faixaConsumo.FaixaConsumo;
import com.br.desafio.ras.modules.tabelaTarifaria.TabelaTarifaria;
import com.br.desafio.ras.modules.tabelaTarifaria.TabelaTarifariaRepository;
import com.br.desafio.ras.modules.calculo.dto.CalculoRequestDTO;
import com.br.desafio.ras.modules.calculo.dto.CalculoResponseDTO;
import com.br.desafio.ras.modules.utils.DetalhamentoDTO;
import com.br.desafio.ras.modules.faixaConsumo.dto.FaixaInternaDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculoService {

    private final TabelaTarifariaRepository tabelaRepository;

    public CalculoResponseDTO calcular(CalculoRequestDTO request) {

        LocalDate hoje = LocalDate.now();
        // Valida se existe alguma tabela ativa
        TabelaTarifaria tabela = tabelaRepository
                .findByDeleted(0L)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Nenhuma tabela encontrada"));

        // Valida se a data de virgencia é valida
        if (hoje.isBefore(tabela.getDataVigenciaInicio()) ||
                hoje.isAfter(tabela.getDataVigenciaFim())) {

            throw new BusinessRuleException("Tabela encontrada, porém está fora do período de vigência");
        }

        // Valida a existencia da categoria recebida
        CategoriaTarifaria categoria = tabela.getCategorias().stream()
                .filter(c -> c.getNome().name().equals(request.getCategoria()))
                .findFirst()
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Categoria não encontrada:" + request.getCategoria()));


        return calcularProgressivo(categoria, request);
    }


    private CalculoResponseDTO calcularProgressivo(
            CategoriaTarifaria categoria,
            CalculoRequestDTO request) {

        if(request.getConsumo() <= 0){
            throw new BusinessRuleException("O consumo deve ser maior que 0");
        }

        // Ordena as faixas pelo início para garantir processamento sequencial correto
        List<FaixaConsumo> faixas = categoria.getFaixas().stream()
                .sorted(Comparator.comparing(FaixaConsumo::getInicio))
                .toList();

        int consumoRestante = request.getConsumo();

        BigDecimal total = BigDecimal.ZERO;

        // armazena o detalhamento por faixa
        List<DetalhamentoDTO> detalhamento = new ArrayList<>();

        for (FaixaConsumo faixa : faixas) {

            if (consumoRestante <= 0) break;

            int capacidadeFaixa = faixa.getFim() - faixa.getInicio() + 1;

            int m3Cobrados = Math.min(consumoRestante, capacidadeFaixa);

            // Calcula o subtotal da faixa (m³ cobrados × valor unitário)
            BigDecimal subtotal = faixa.getValorUnitario()
                    .multiply(BigDecimal.valueOf(m3Cobrados));

            total = total.add(subtotal);

            // Adiciona o detalhamento da faixa ao resultado
            detalhamento.add(new DetalhamentoDTO(
                    new FaixaInternaDTO(faixa.getInicio(), faixa.getFim()),
                    m3Cobrados,
                    faixa.getValorUnitario(),
                    subtotal
            ));

            consumoRestante -= m3Cobrados;
        }
        // Retorna o resultado consolidado do cálculo
        return new CalculoResponseDTO(
                request.getCategoria(),
                request.getConsumo(),
                total,
                detalhamento
        );
    }
}

