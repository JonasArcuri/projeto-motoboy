package com.example.projeto_motoboy.dto;

import lombok.Data;

@Data
public class EstabelecimentoDTO {
    private Long id;
    private String cnpj;
    private String nomeFantasia;
    private String categoria;
    private String horarioFuncionamento;
    private String telefone;
    private String descricao;
}
