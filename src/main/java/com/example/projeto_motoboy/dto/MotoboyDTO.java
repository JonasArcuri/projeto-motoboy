package com.example.projeto_motoboy.dto;

import lombok.Data;

@Data
public class MotoboyDTO {
    private Long id;
    private String cpf;
    private String cnh;
    private String placaMoto;
    private String modeloMoto;
    private Integer anoMoto;
    private String experiencia;
    private String telefoneContato;
}
