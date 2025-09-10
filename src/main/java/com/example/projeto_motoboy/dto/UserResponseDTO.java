package com.example.projeto_motoboy.dto;

import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;
    private String tipo;
    private EstabelecimentoDTO estabelecimento;
    private MotoboyDTO motoboy;
}
