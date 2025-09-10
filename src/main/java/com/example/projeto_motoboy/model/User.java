// ========== 1. ENTIDADE USER (ATUALIZADA) ==========
package com.example.projeto_motoboy.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String name;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O e-mail deve ser válido")
    @Size(max = 100, message = "Email deve ter no máximo 100 caracteres")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    private String password;

    @Size(max = 100, message = "Endereço deve ter no máximo 100 caracteres")
    private String endereco;

    @Size(max = 40, message = "Bairro deve ter no máximo 40 caracteres")
    private String bairro;

    @Size(max = 50, message = "Cidade deve ter no máximo 50 caracteres")
    private String cidade;

    @Size(max = 2, message = "UF deve ter 2 caracteres")
    private String uf;

    @Size(max = 8, message = "CEP deve ter 8 dígitos")
    private String cep;

    @NotBlank(message = "O tipo é obrigatório")
    @Column(name = "tipo")
    private String tipo; // "Estabelecimento" ou "Motoboy"

    // Relacionamentos (Lazy loading para performance)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference("user-estabelecimento")
    private Estabelecimento estabelecimento;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @JsonManagedReference("user-motoboy")
    private Motoboy motoboy;

    // Métodos auxiliares
    public boolean isEstabelecimento() {
        return "Estabelecimento".equals(this.tipo);
    }

    public boolean isMotoboy() {
        return "Motoboy".equals(this.tipo);
    }
}