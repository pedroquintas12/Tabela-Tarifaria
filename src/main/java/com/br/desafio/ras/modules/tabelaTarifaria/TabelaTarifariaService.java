package com.br.desafio.ras.modules.tabelaTarifaria;

import com.br.desafio.ras.core.exception.BusinessRuleException;
import com.br.desafio.ras.core.exception.RecursoNaoEncontradoException;
import com.br.desafio.ras.enums.CategoriaEnum;
import com.br.desafio.ras.modules.categoriaTarifaria.CategoriaTarifaria;
import com.br.desafio.ras.modules.categoriaTarifaria.dto.CategoriaResponseDTO;
import com.br.desafio.ras.modules.faixaConsumo.FaixaConsumo;
import com.br.desafio.ras.modules.faixaConsumo.dto.FaixaDTO;
import com.br.desafio.ras.modules.faixaConsumo.dto.FaixaResponseDTO;
import com.br.desafio.ras.modules.tabelaTarifaria.dto.TabelaTarifariaResponseDTO;
import com.br.desafio.ras.modules.tabelaTarifaria.dto.criarTabelaTarifariaDTO;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TabelaTarifariaService {

    private final TabelaTarifariaRepository tabelaRepository;

    //CRIAR TABELA

    @Transactional
    public void criarTabela(criarTabelaTarifariaDTO dto) {

        validarDadosRecebidos(dto);

        validarExistenciaTabelaAtiva();
        validarPeriodoVigencia(dto);

        TabelaTarifaria tabela = mapearParaEntidade(dto);

        tabelaRepository.save(tabela);
    }


    private void validarNumeroNaoNulo(Integer campo, String mensagem) {

        if (campo == null || campo < 0) {
            throw new BusinessRuleException(mensagem);
        }
    }
    private void validarTextoObrigatorio(String campo, String mensagem) {
        if (campo == null || campo.isBlank()) {
            throw new BusinessRuleException(mensagem);
        }
    }

    private void validarListaObrigatoria(Collection<?> lista, String mensagem) {
        if (lista == null || lista.isEmpty()) {
            throw new BusinessRuleException(mensagem);
        }
    }

    private void validarDadosRecebidos(criarTabelaTarifariaDTO dto) {

        if(dto == null){
            throw new BusinessRuleException("Requisição inválida");
        }

        validarTextoObrigatorio(dto.getNome(), "Nome da tabela é obrigatório");

        validarListaObrigatoria(dto.getCategorias(), "Categorias são obrigatórias");

        dto.getCategorias().forEach(categoria -> {

            validarTextoObrigatorio(
                    categoria.getNome(),
                    "Nome da categoria é obrigatório"
            );

            validarListaObrigatoria(
                    categoria.getFaixas(),
                    "Faixas são obrigatórias"
            );

            categoria.getFaixas().forEach(faixa -> {

                validarNumeroNaoNulo(faixa.getInicio(), "Início é obrigatório e nao deve ser negativo");
                validarNumeroNaoNulo(faixa.getFim(), "Fim é obrigatório");
                if (faixa.getValorUnitario() == null || faixa.getValorUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                    throw new BusinessRuleException("Valor unitário deve ser maior que zero");
                }            });
        });
    }

    private void validarExistenciaTabelaAtiva() {
        if (tabelaRepository.existsByDeleted(0L)) {
            throw new BusinessRuleException(
                    "Já existe uma tabela ativa. Desative a atual antes de criar outra."
            );
        }

    }

    private void validarPeriodoVigencia(criarTabelaTarifariaDTO dto) {

        if (dto.getDataVigenciaInicio() == null || dto.getDataVigenciaFim() == null) {
            throw new BusinessRuleException("Datas de vigência são obrigatórias.");
        }

        if (dto.getDataVigenciaFim().isBefore(dto.getDataVigenciaInicio())) {
            throw new BusinessRuleException(
                    "Data de fim deve ser posterior à data de início."
            );
        }

        if (dto.getDataVigenciaFim().isBefore(LocalDate.now())) {
            throw new BusinessRuleException(
                    "Não é permitido criar tabela com vigência já expirada."
            );
        }
    }


    private TabelaTarifaria mapearParaEntidade(criarTabelaTarifariaDTO dto) {

        TabelaTarifaria tabela = new TabelaTarifaria();
        tabela.setNome(dto.getNome());
        tabela.setDataVigenciaInicio(dto.getDataVigenciaInicio());
        tabela.setDataVigenciaFim(dto.getDataVigenciaFim());

        Set<CategoriaTarifaria> categorias = dto.getCategorias().stream()
                .map(catDto -> {

                    validarFaixas(catDto.getFaixas());

                    CategoriaTarifaria categoria = new CategoriaTarifaria();
                    categoria.setNome(parseCategoria(catDto.getNome()));
                    categoria.setTabelaTarifaria(tabela);

                    Set<FaixaConsumo> faixas = catDto.getFaixas().stream()
                            .map(faixaDto -> {

                                FaixaConsumo faixa = new FaixaConsumo();
                                faixa.setInicio(faixaDto.getInicio());
                                faixa.setFim(faixaDto.getFim());
                                faixa.setValorUnitario(faixaDto.getValorUnitario());
                                faixa.setCategoriaTarifaria(categoria);

                                return faixa;

                            })
                            .collect(Collectors.toSet());

                    categoria.setFaixas(faixas);
                    return categoria;

                })
                .collect(Collectors.toSet());

        tabela.setCategorias(categorias);

        return tabela;
    }

    private CategoriaEnum parseCategoria(String nome) {
        try {
            return CategoriaEnum.valueOf(nome.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new BusinessRuleException("Categoria inválida: " + nome);
        }
    }

    @Transactional(readOnly = true)
    public List<TabelaTarifariaResponseDTO> listar() {

        return tabelaRepository.findByDeleted(0L)
                .stream()
                .map(this::mapearParaResponse)
                .toList();
    }

    private TabelaTarifariaResponseDTO mapearParaResponse(TabelaTarifaria tabela) {

        return new TabelaTarifariaResponseDTO(
                tabela.getId(),
                tabela.getNome(),
                tabela.getDataVigenciaInicio(),
                tabela.getDataVigenciaFim(),
                tabela.getCategorias().stream()
                        .filter(c -> c.getDeleted() == 0L)
                        .map(categoria -> new CategoriaResponseDTO(
                                categoria.getNome().name(),
                                categoria.getFaixas().stream()
                                        .filter(f -> f.getDeleted() == 0L)
                                        .map(faixa -> new FaixaResponseDTO(
                                                faixa.getInicio(),
                                                faixa.getFim(),
                                                faixa.getValorUnitario()
                                        ))
                                        .toList()
                        ))
                        .toList()
        );
    }

    // DESATIVAR (SOFT DELETE)
    @Transactional
    public void desativar(Long id) {

        TabelaTarifaria tabela = tabelaRepository.findById(id)
                .orElseThrow(() ->
                        new RecursoNaoEncontradoException("Tabela não encontrada")
                );

        tabela.setDeleted(1L);

        tabela.getCategorias().forEach(categoria -> {
            categoria.setDeleted(1L);
            categoria.getFaixas().forEach(faixa -> faixa.setDeleted(1L));
        });
    }

    //VALIDAÇÃO DE FAIXAS

    private void validarFaixas(List<FaixaDTO> faixas) {

        if (faixas == null || faixas.isEmpty()) {
            throw new BusinessRuleException("Faixas não podem ser vazias");
        }

        List<FaixaDTO> ordenadas = faixas.stream()
                .sorted(Comparator.comparing(FaixaDTO::getInicio))
                .toList();

        if (ordenadas.get(0).getInicio() != 0) {
            throw new BusinessRuleException("Primeira faixa deve iniciar em 0");
        }

        for (int i = 0; i < ordenadas.size(); i++) {

            FaixaDTO atual = ordenadas.get(i);

            if (atual.getInicio() >= atual.getFim()) {
                throw new BusinessRuleException("Início deve ser menor que fim");
            }

            if (i > 0) {
                FaixaDTO anterior = ordenadas.get(i - 1);

                if (anterior.getFim() + 1 != atual.getInicio()) {
                    throw new BusinessRuleException(
                            "Faixas devem ser contínuas e sem sobreposição"
                    );
                }
            }
        }
    }
}

