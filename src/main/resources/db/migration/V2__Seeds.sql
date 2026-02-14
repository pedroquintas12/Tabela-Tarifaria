
INSERT INTO tabela_tarifaria (
    nome,
    data_vigencia_inicio,
    data_vigencia_fim,
    deleted
) VALUES (
             'Tabela Seed 2026',
             CURRENT_DATE - INTERVAL '1 day',
             CURRENT_DATE + INTERVAL '365 day',
             0
         );

INSERT INTO categoria_tarifaria (
    nome,
    tabela_tarifaria_id,
    deleted
)
SELECT
    nome_categoria,
    t.tabela_tarifa_id,
    0
FROM (
         VALUES
             ('INDUSTRIAL'),
             ('COMERCIAL'),
             ('PARTICULAR'),
             ('PUBLICO')
     ) AS categorias(nome_categoria)
         CROSS JOIN tabela_tarifaria t
WHERE t.deleted = 0;


INSERT INTO faixa_consumo (
    inicio,
    fim,
    valor_unitario,
    categoria_tarifaria_id,
    deleted
)
SELECT
    f.inicio,
    f.fim,
    f.valor,
    c.categoria_tarifa_id,
    0
FROM categoria_tarifaria c
         JOIN (
    VALUES
        (0, 10, 10.00),
        (11, 20, 20.00),
        (21, 99999, 30.00)
) AS f(inicio, fim, valor)
              ON true
WHERE c.deleted = 0;
