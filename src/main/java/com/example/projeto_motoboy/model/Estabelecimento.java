// ========== 2. ENTIDADE ESTABELECIMENTO ==========
package com.example.projeto_motoboy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@Table(name = "estabelecimentos")
public class Estabelecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference("user-estabelecimento")
    private User user;

    @NotBlank(message = "CNPJ é obrigatório")
    @Size(max = 18, message = "CNPJ deve ter no máximo 18 caracteres")
    @Column(unique = true)
    private String cnpj;

    @NotBlank(message = "Nome fantasia é obrigatório")
    @Size(max = 100, message = "Nome fantasia deve ter no máximo 100 caracteres")
    private String nomeFantasia;

    @Size(max = 50, message = "Categoria deve ter no máximo 50 caracteres")
    private String categoria;

    @Size(max = 100, message = "Horário deve ter no máximo 100 caracteres")
    private String horarioFuncionamento;

    @NotBlank(message = "Telefone é obrigatório")
    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    private String telefone;

    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    @Column(columnDefinition = "TEXT")
    private String descricao;
}