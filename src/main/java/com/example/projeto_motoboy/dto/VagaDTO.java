package com.example.projeto_motoboy.dto;

import lombok.Data;

@Data
public class VagaDTO {
    private Long id;
    private String descricao;
    private String endereco;
    private String valor;
    private String taxas;
    private String horario;
    private String zona;
    private String tipo;
    private Long estabelecimentoId;
}
