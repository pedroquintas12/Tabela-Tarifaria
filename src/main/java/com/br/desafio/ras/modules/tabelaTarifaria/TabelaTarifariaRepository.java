package com.br.desafio.ras.modules.tabelaTarifaria;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface TabelaTarifariaRepository
        extends JpaRepository<TabelaTarifaria, Long> {

    @EntityGraph(attributePaths = {
            "categorias",
            "categorias.faixas"
    })
    Optional<TabelaTarifaria> findByDeleted(Long deleted);


   Boolean existsByDeleted(Long deleted);


    @Query("""
    SELECT t FROM TabelaTarifaria t
    WHERE t.deleted = 0
      AND :hoje BETWEEN t.dataVigenciaInicio AND t.dataVigenciaFim
""")
    Optional<TabelaTarifaria> findTabelaVigente(LocalDate hoje);


}