package com.br.desafio.ras.modules.categoriaTarifaria;


import com.br.desafio.ras.core.jpa.AbstractPersistableCustom;
import com.br.desafio.ras.enums.CategoriaEnum;
import com.br.desafio.ras.modules.faixaConsumo.FaixaConsumo;
import com.br.desafio.ras.modules.tabelaTarifaria.TabelaTarifaria;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categoria_tarifaria")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@AttributeOverride(name = "id", column = @Column(name = "categoria_tarifa_id"))
@Where(clause = "deleted=0")
public class CategoriaTarifaria extends AbstractPersistableCustom<Long> {


    @NotBlank(message = "Nome da categoria obrigatoria")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(example = "RESIDENCIAL")
    private CategoriaEnum nome;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tabela_tarifaria_id", nullable = false)
    private TabelaTarifaria tabelaTarifaria;

    @OneToMany(mappedBy = "categoriaTarifaria",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<FaixaConsumo> faixas = new HashSet<>();



}
