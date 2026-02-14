package com.br.desafio.ras.modules.faixaConsumo;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FaixaConsumoRepository
        extends JpaRepository<FaixaConsumo, Long> {

    Optional<FaixaConsumo> findByCategoriaTarifariaId(Long id);


}
