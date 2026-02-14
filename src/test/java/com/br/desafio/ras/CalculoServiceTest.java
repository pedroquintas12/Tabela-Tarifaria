package com.br.desafio.ras;

import com.br.desafio.ras.core.exception.BusinessRuleException;
import com.br.desafio.ras.core.exception.RecursoNaoEncontradoException;
import com.br.desafio.ras.enums.CategoriaEnum;
import com.br.desafio.ras.modules.calculo.CalculoService;
import com.br.desafio.ras.modules.categoriaTarifaria.CategoriaTarifaria;
import com.br.desafio.ras.modules.faixaConsumo.FaixaConsumo;
import com.br.desafio.ras.modules.tabelaTarifaria.TabelaTarifaria;
import com.br.desafio.ras.modules.tabelaTarifaria.TabelaTarifariaRepository;
import com.br.desafio.ras.modules.calculo.dto.CalculoRequestDTO;
import com.br.desafio.ras.modules.calculo.dto.CalculoResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

class CalculoServiceTest {

    @Mock
    private TabelaTarifariaRepository tabelaRepository;

    @InjectMocks
    private CalculoService calculoService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveCalcularValorProgressivoCorretamente() {

        // cenário

        FaixaConsumo faixa1 = new FaixaConsumo();
        faixa1.setInicio(0);
        faixa1.setFim(10);
        faixa1.setValorUnitario(BigDecimal.valueOf(2.0));

        FaixaConsumo faixa2 = new FaixaConsumo();
        faixa2.setInicio(11);
        faixa2.setFim(20);
        faixa2.setValorUnitario(BigDecimal.valueOf(3.0));

        CategoriaTarifaria categoria = new CategoriaTarifaria();
        categoria.setNome(CategoriaEnum.COMERCIAL);
        categoria.setFaixas(Set.of(faixa1, faixa2));

        TabelaTarifaria tabela = new TabelaTarifaria();
        tabela.setDeleted(0L);
        tabela.setDataVigenciaInicio(LocalDate.now().minusDays(1));
        tabela.setDataVigenciaFim(LocalDate.now().plusDays(10));
        tabela.setCategorias(Set.of(categoria));

        when(tabelaRepository.findByDeleted(0L))
                .thenReturn(Optional.of(tabela));

        CalculoRequestDTO request = new CalculoRequestDTO();
        request.setCategoria("COMERCIAL");
        request.setConsumo(15);

        // execução

        CalculoResponseDTO response = calculoService.calcular(request);

        // validação

        // Faixa 1 -> 11 m³ (0-10) x 2.0 = 22
        // Faixa 2 -> 4 m³ (11-14) x 3.0 = 12
        // Total esperado = 34

        assertEquals(15, response.getConsumoTotal());
        assertEquals(BigDecimal.valueOf(34.0), response.getValorTotal());
        assertEquals("COMERCIAL", response.getCategoria());
        assertEquals(2, response.getDetalhamento().size());
    }

    @Test
    void deveLancarExcecaoQuandoNaoExistirTabela() {

        when(tabelaRepository.findByDeleted(0L))
                .thenReturn(Optional.empty());

        CalculoRequestDTO request = new CalculoRequestDTO();
        request.setCategoria("COMERCIAL");
        request.setConsumo(10);

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> calculoService.calcular(request)
        );
    }
}
