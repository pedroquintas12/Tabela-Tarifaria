package com.br.desafio.ras.core.http;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseHttpDTO<T> implements Serializable {

    private Integer status;
    private String message;
    @Builder.Default
    private Long timestamp = new Date().getTime();
}