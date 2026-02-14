package com.br.desafio.ras.modules.faixaConsumo;

import com.br.desafio.ras.core.jpa.AbstractPersistableCustom;
import com.br.desafio.ras.modules.categoriaTarifaria.CategoriaTarifaria;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.SuperBuilder;

import org.hibernate.annotations.Where;

import java.math.BigDecimal;

@Entity
@Table(name = "faixa_consumo",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"categoria_tarifaria_id", "inicio", "fim"}
                )
        })
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@AttributeOverride(name = "id", column = @Column(name = "faixa_consumo_id"))
@Where(clause = "deleted=0")
public class FaixaConsumo extends AbstractPersistableCustom<Long>{

    @NotBlank(message = "Inicio da faixa obrigatorio")
    @Column(nullable = false)
    private Integer inicio;

    @NotBlank(message = "Fim da faixa obrigatorio")
    @Column(nullable = false)
    private Integer fim;

    @NotBlank(message = "valor unitário obrigatorio")
    @Positive(message = "Valor unitário deve ser positivo")
    @Column(name = "valor_unitario",
            nullable = false,
            precision = 10,
            scale = 2)
    private BigDecimal valorUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_tarifaria_id", nullable = false)
    private CategoriaTarifaria categoriaTarifaria;

}
