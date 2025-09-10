// ========== 3. ENTIDADE MOTOBOY ==========
package com.example.projeto_motoboy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Data
@Table(name = "motoboys")
public class Motoboy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonBackReference("user-motoboy")
    private User user;

    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter 11 dígitos")
    @Column(unique = true)
    private String cpf;

    @NotBlank(message = "CNH é obrigatória")
    @Size(max = 15, message = "CNH deve ter no máximo 15 caracteres")
    private String cnh;

    @NotBlank(message = "Placa da moto é obrigatória")
    @Size(max = 8, message = "Placa deve ter no máximo 8 caracteres")
    private String placaMoto;

    @NotBlank(message = "Modelo da moto é obrigatório")
    @Size(max = 50, message = "Modelo deve ter no máximo 50 caracteres")
    private String modeloMoto;

    @Min(value = 1990, message = "Ano deve ser maior que 1990")
    @Max(value = 2030, message = "Ano deve ser menor que 2030")
    private Integer anoMoto;

    @Size(max = 30, message = "Experiência deve ter no máximo 30 caracteres")
    private String experiencia;

    @NotBlank(message = "Telefone de contato é obrigatório")
    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    private String telefoneContato;
}