package com.br.desafio.ras.core.exception;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
public class ErroResponse {

    private Integer status;
    private String mensagem;
    private LocalDateTime timestamp;

    public ErroResponse(Integer status,
                        String mensagem,
                        LocalDateTime timestamp) {
        this.status = status;
        this.mensagem = mensagem;
        this.timestamp = timestamp;
    }

}
