package com.br.desafio.ras.modules.categoriaTarifaria;



import com.br.desafio.ras.enums.CategoriaEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoriaTarifariaRepository
        extends JpaRepository<CategoriaTarifaria, Long> {

    Optional<CategoriaTarifaria> findByNomeAndTabelaTarifaria_Deleted(
            CategoriaEnum nome,
            Long deleted);

    Optional<CategoriaTarifaria> findByTabelaTarifariaId( Long id);

}


