package com.br.desafio.ras.modules.tabelaTarifaria;


import com.br.desafio.ras.core.jpa.AbstractPersistableCustom;
import com.br.desafio.ras.modules.categoriaTarifaria.CategoriaTarifaria;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Where;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tabela_tarifaria")
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
@AttributeOverride(name = "id", column = @Column(name = "tabela_tarifa_id"))
@Where(clause = "deleted=0")
public class TabelaTarifaria extends AbstractPersistableCustom<Long> {


    @Column(nullable = false)
    @NotBlank(message = "Nome da tabela obrigatorio")
    private String nome;

    @Column(name = "data_vigencia_inicio", nullable = false)
    private LocalDate dataVigenciaInicio;

    @Column(name = "data_vigencia_fim")
    private LocalDate dataVigenciaFim;

    @OneToMany(mappedBy = "tabelaTarifaria",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<CategoriaTarifaria> categorias = new HashSet<>();

}
