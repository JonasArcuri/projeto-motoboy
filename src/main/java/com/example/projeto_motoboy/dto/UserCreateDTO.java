package com.example.projeto_motoboy.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserCreateDTO {
    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

    private String endereco;
    private String bairro;
    private String cidade;
    private String uf;
    private String cep;

    @NotBlank(message = "Tipo é obrigatório")
    private String tipo;

    // Campos do estabelecimento (opcionais)
    private String cnpj;
    private String nomeFantasia;
    private String categoria;
    private String horarioFuncionamento;
    private String telefone;
    private String descricao;

    // Campos do motoboy (opcionais)
    private String cpf;
    private String cnh;
    private String placaMoto;
    private String modeloMoto;
    private Integer anoMoto;
    private String experiencia;
    private String telefoneContato;
}

