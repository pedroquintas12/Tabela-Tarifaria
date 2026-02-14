CREATE TABLE tabela_tarifaria (
            tabela_tarifa_id BIGSERIAL PRIMARY KEY,
            nome VARCHAR(255) NOT NULL,
            data_vigencia_inicio DATE NOT NULL,
            data_vigencia_fim DATE,
            created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
            deleted bigint NOT NULL DEFAULT '0'
);
CREATE TABLE categoria_tarifaria (
                categoria_tarifa_id BIGSERIAL PRIMARY KEY,
                nome VARCHAR(50) NOT NULL,
                tabela_tarifaria_id BIGINT NOT NULL,
                created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                deleted bigint NOT NULL DEFAULT '0',
                CONSTRAINT fk_tabela
                    FOREIGN KEY (tabela_tarifaria_id)
                     REFERENCES tabela_tarifaria(tabela_tarifa_id)
                        ON DELETE CASCADE
);

CREATE TABLE faixa_consumo (
                faixa_consumo_id BIGSERIAL PRIMARY KEY,
                inicio INTEGER NOT NULL,
                fim INTEGER NOT NULL,
                valor_unitario NUMERIC(10,2) NOT NULL,
                categoria_tarifaria_id BIGINT NOT NULL,
                created_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                modified_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                deleted bigint NOT NULL DEFAULT '0',
                CONSTRAINT fk_categoria
                    FOREIGN KEY (categoria_tarifaria_id)
                    REFERENCES categoria_tarifaria(categoria_tarifa_id)
                    ON DELETE CASCADE,
                    CONSTRAINT faixa_valida CHECK (inicio < fim)
);
