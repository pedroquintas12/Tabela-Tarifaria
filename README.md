# Desafio grupo ras desenvolvedor junior

API REST desenvolvida em **Spring Boot 3 + PostgreSQL**, responsável por:

- Cadastro de tabela tarifária
- Cadastro de categorias e faixas de consumo
- softdelete da tabela tarifaria e seus relacionados
- Cálculo progressivo de consumo
- Documentação via Swagger

---

# Tecnologias Utilizadas

- Java 17
- Spring Boot 3.5.10
- Spring Data JPA
- Hibernate 6
- PostgreSQL 16
- Flyway (migrations)
- Swagger / OpenAPI (springdoc)
- Lombok
- JUnit 5 + Mockito
- Docker

---

# Pré-requisitos

## Para rodar SEM Docker

- Java 17
- Maven 3.9+
- PostgreSQL 16+

## Para rodar COM Docker

- Docker 24+
- Docker Compose

---

# Arquitetura do Projeto

O projeto segue uma arquitetura modular baseada em feature, onde cada domínio de negócio possui seu próprio pacote contendo:

- Controller (Resources)
- Service
- Repository
- DTOs
- Entidades

Além disso, existe um módulo `core` responsável por:

- Tratamento global de exceções
- Padronização de respostas HTTP
- Abstrações JPA comuns

---

# Regra de Negócio

## Criação de Tabela

- Apenas **uma tabela ativa** pode existir por vez.
- A primeira faixa deve iniciar obrigatoriamente em **0**.
- As faixas devem ser contínuas e sem sobreposição.
- O valor unitário deve ser maior que zero.
- Não é permitido criar tabela com vigência expirada.
- Datas de vigência são obrigatórias.

## Cálculo Progressivo

O cálculo é realizado de forma progressiva:

- Cada faixa cobra apenas o consumo correspondente ao seu intervalo.
- O valor total é a soma dos subtotais de cada faixa aplicada.

---

# Como Rodar a Aplicação


## OPÇÃO 1 — Rodar SEM Docker

### 1 Criar o banco de dados

No PostgreSQL:

```sql
CREATE DATABASE tarifas
```

### credenciais:
> Se você já possui PostgreSQL instalado, utilize suas próprias credenciais.

## 3 Subir aplicação

No terminal:
```terminaloutput
 mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

# OPÇÃO 2 - Rodar em Docker

```terminaloutput
docker compose up -d --build
```
## Credenciais do banco docker:

    database:tarifas
    username: grupo_ras
    password: 123456

## aplicação disponivel em:
http://localhost:8080

---

# Exemplo de Requests:

A documentação detalhada da API, incluindo todos os códigos de erro e modelos, pode ser acessada via Swagger:

http://localhost:8080/swagger-ui.html

## Criar tabela:

### POST ```/api/tabelas-tarifarias```

#### Request Body
201 - CREATED
```json
{
  "nome": "Tabela 2026",
  "dataVigenciaInicio": "2026-01-01",
  "dataVigenciaFim": "2026-12-31",
  "categorias": [
    {
      "nome": "INDUSTRIAL",
      "faixas": [
        {
          "inicio": 0,
          "fim": 10,
          "valorUnitario": 13.00
        },
        {
          "inicio": 11,
          "fim": 20,
          "valorUnitario": 22.00
        },
        {
          "inicio": 21,
          "fim": 99999,
          "valorUnitario": 32.00
        }
      ]
    },
    {
      "nome": "COMERCIAL",
      "faixas": [
        {
          "inicio": 0,
          "fim": 10,
          "valorUnitario": 1.50
        },
        {
          "inicio": 11,
          "fim": 20,
          "valorUnitario": 2.50
        },
        {
          "inicio": 21,
          "fim": 99999,
          "valorUnitario": 3.50
        }
      ]
    },
    {
      "nome": "PARTICULAR",
      "faixas": [
        {
          "inicio": 0,
          "fim": 10,
          "valorUnitario": 0.80
        },
        {
          "inicio": 11,
          "fim": 20,
          "valorUnitario": 1.20
        },
        {
          "inicio": 21,
          "fim": 99999,
          "valorUnitario": 2.00
        }
      ]
    },
    {
      "nome": "PUBLICO",
      "faixas": [
        {
          "inicio": 0,
          "fim": 10,
          "valorUnitario": 0.50
        },
        {
          "inicio": 11,
          "fim": 20,
          "valorUnitario": 1.00
        },
        {
          "inicio": 21,
          "fim": 99999,
          "valorUnitario": 1.50
        }
      ]
    }
  ]
}
```

#### Response Body:

```201 - CRETED```
```json
{
  "status": 201,
  "message": "Tabela criada com sucesso!"
}
```

```409 - CONFLICT```(violação de regra de negócio)

### GET ```/api/tabelas-tarifarias```

#### Response Body

```json
[
    {
        "id": 2,
        "nome": "Tabela 2026",
        "dataVigenciaInicio": "2026-01-01",
        "dataVigenciaFim": "2026-12-31",
        "categorias": [
            {
                "nome": "PUBLICO",
                "faixas": [
                    {
                        "inicio": 21,
                        "fim": 99999,
                        "valorUnitario": 1.50
                    },
                    {
                        "inicio": 11,
                        "fim": 20,
                        "valorUnitario": 1.00
                    },
                    {
                        "inicio": 0,
                        "fim": 10,
                        "valorUnitario": 0.50
                    }
                ]
            },
            {
                "nome": "PARTICULAR",
                "faixas": [
                    {
                        "inicio": 21,
                        "fim": 99999,
                        "valorUnitario": 2.00
                    },
                    {
                        "inicio": 11,
                        "fim": 20,
                        "valorUnitario": 1.20
                    },
                    {
                        "inicio": 0,
                        "fim": 10,
                        "valorUnitario": 0.80
                    }
                ]
            },
            {
                "nome": "INDUSTRIAL",
                "faixas": [
                    {
                        "inicio": 11,
                        "fim": 20,
                        "valorUnitario": 22.00
                    },
                    {
                        "inicio": 0,
                        "fim": 10,
                        "valorUnitario": 13.00
                    },
                    {
                        "inicio": 21,
                        "fim": 99999,
                        "valorUnitario": 32.00
                    }
                ]
            },
            {
                "nome": "COMERCIAL",
                "faixas": [
                    {
                        "inicio": 21,
                        "fim": 99999,
                        "valorUnitario": 3.50
                    },
                    {
                        "inicio": 11,
                        "fim": 20,
                        "valorUnitario": 2.50
                    },
                    {
                        "inicio": 0,
                        "fim": 10,
                        "valorUnitario": 1.50
                    }
                ]
            }
        ]
    }
]
```
### DELETE ```/api/tabelas-tarifarias/{id}```

#### Response Body

```204 - No Content```

```404 - NOT FOUND```
```json
{
"status": 404,
"message": "Tabela não encontrada",
"timestamp": "2026-02-13T17:00:00"
}
```

### POST ```/api/calculos```

#### Request Body:

```json
{
    "categoria":"INDUSTRIAL",
    "consumo":1
}
```

#### Response Body:
```200 - OK```
```json
{
    "categoria": "INDUSTRIAL",
    "consumoTotal": 1,
    "valorTotal": 13.00,
    "detalhamento": [
        {
            "faixa": {
                "inicio": 0,
                "fim": 10
            },
            "m3Cobrados": 1,
            "valorUnitario": 13.00,
            "subtotal": 13.00
        }
    ]
}

```
```409 - CONFLICT```(violação de regra de negócio)

---

# Executanto testes
```terminaloutput
mvn clean test
```
---
## O projeto possui  testes unitários para:

- Criação de tabela
- Soft Delete
- Regras de validação
- Cálculo progressivo
---

# Versionamento do banco 

### As migrations estão localizadas em:
```
src/main/resources/db/migration
```
#### Executadas automaticamente pelo Flayway na inicialização da aplicação

# Tratamento de erros

## A aplicação possui:
- Handler glogal de exceções
- Padronização de resostas de erro
- Separação entre:
  - 409(regra de negócio)
  - 404(recurso não encontrado)
  - 500 (erro inesperado)


