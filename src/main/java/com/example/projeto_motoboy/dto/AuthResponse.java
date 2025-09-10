package com.example.projeto_motoboy.dto;

public class AuthResponse {
    private Long id;
    private String name;
    private String email;
    private String tipo;
    private String token; // Na verdade é a mensagem, mas mantemos para compatibilidade

    // Construtor
    public AuthResponse(Long id, String name, String email, String tipo, String message) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.tipo = tipo;
        this.token = message; // Aqui colocamos a mensagem
    }

    // Construtor vazio (necessário para Jackson)
    public AuthResponse() {}

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "AuthResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", tipo='" + tipo + '\'' +
                ", token='" + token + '\'' +
                '}';
    }
}