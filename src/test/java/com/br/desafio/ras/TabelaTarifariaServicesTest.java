package com.br.desafio.ras;


import com.br.desafio.ras.core.exception.BusinessRuleException;
import com.br.desafio.ras.core.exception.RecursoNaoEncontradoException;
import com.br.desafio.ras.modules.categoriaTarifaria.CategoriaTarifaria;
import com.br.desafio.ras.modules.faixaConsumo.FaixaConsumo;
import com.br.desafio.ras.modules.tabelaTarifaria.TabelaTarifaria;
import com.br.desafio.ras.modules.tabelaTarifaria.TabelaTarifariaRepository;
import com.br.desafio.ras.modules.tabelaTarifaria.TabelaTarifariaService;
import com.br.desafio.ras.modules.tabelaTarifaria.dto.criarTabelaTarifariaDTO;
import com.br.desafio.ras.modules.categoriaTarifaria.dto.CategoriaDTO;
import com.br.desafio.ras.modules.faixaConsumo.dto.FaixaDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TabelaTarifariaServicesTest {

    @Mock
    private TabelaTarifariaRepository tabelaRepository;

    @InjectMocks
    private TabelaTarifariaService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    //criarTabela

    @Test
    void deveCriarTabelaComSucesso() {

        when(tabelaRepository.existsByDeleted(0L))
                .thenReturn(false);

        FaixaDTO faixa = FaixaDTO.builder()
                .inicio(0)
                .fim(10)
                .valorUnitario(BigDecimal.valueOf((2.5)))
                .build();

        CategoriaDTO categoria = CategoriaDTO.builder()
                .nome("COMERCIAL")
                .faixas(List.of(faixa))
                .build();

        criarTabelaTarifariaDTO dto = criarTabelaTarifariaDTO.builder()
                .nome("Tabela 2026")
                .dataVigenciaInicio(LocalDate.now())
                .dataVigenciaFim(LocalDate.now().plusDays(10))
                .categorias(Set.of(categoria))
                .build();

        service.criarTabela(dto);

        verify(tabelaRepository, times(1)).save(any());
    }

    @Test
    void naoDeveCriarTabelaComVigenciaExpirada() {

        when(tabelaRepository.existsByDeleted(0L))
                .thenReturn(false);

        criarTabelaTarifariaDTO dto = criarTabelaTarifariaDTO.builder()
                .nome("Tabela 2026")
                .dataVigenciaInicio(LocalDate.now().minusDays(10))
                .dataVigenciaFim(LocalDate.now().minusDays(1))
                .categorias(Set.of())
                .build();

        assertThrows(
                BusinessRuleException.class,
                () -> service.criarTabela(dto)
        );
    }

    @Test
    void naoDeveCriarTabelaSeJaExistirAtiva() {

        when(tabelaRepository.existsByDeleted(0L))
                .thenReturn(true);

        criarTabelaTarifariaDTO dto = mock(criarTabelaTarifariaDTO.class);

        assertThrows(
                BusinessRuleException.class,
                () -> service.criarTabela(dto)
        );

        verify(tabelaRepository, never()).save(any());
    }

    // DELETE
    @Test
    void deveRealizarSoftDeleteEmCascata() {

        FaixaConsumo faixa = new FaixaConsumo();
        CategoriaTarifaria categoria = new CategoriaTarifaria();
        categoria.setFaixas(Set.of(faixa));

        TabelaTarifaria tabela = new TabelaTarifaria();
        tabela.setCategorias(Set.of(categoria));

        when(tabelaRepository.findById(1L))
                .thenReturn(Optional.of(tabela));

        service.desativar(1L);

        assertEquals(1L, tabela.getDeleted());
        assertEquals(1L, categoria.getDeleted());
        assertEquals(1L, faixa.getDeleted());
    }

    @Test
    void deveLancarExcecaoSeTabelaNaoExistirAoDesativar() {

        when(tabelaRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                RecursoNaoEncontradoException.class,
                () -> service.desativar(99L)
        );
    }
}
