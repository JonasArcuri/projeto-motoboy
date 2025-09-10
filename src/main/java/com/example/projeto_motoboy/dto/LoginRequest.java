package com.example.projeto_motoboy.dto;

public class LoginRequest {
    private String email;
    private String password;

    // Construtor vazio
    public LoginRequest() {}

    // Construtor com parâmetros
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters e Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Método para validar os dados
    public boolean isValid() {
        return email != null && !email.trim().isEmpty() && email.contains("@") &&
                password != null && !password.trim().isEmpty() && password.length() >= 6;
    }

    // Método para obter mensagem de erro de validação
    public String getValidationError() {
        if (email == null || email.trim().isEmpty()) {
            return "Email é obrigatório";
        }
        if (!email.contains("@")) {
            return "Email deve ter formato válido";
        }
        if (password == null || password.trim().isEmpty()) {
            return "Senha é obrigatória";
        }
        if (password.length() < 6) {
            return "Senha deve ter pelo menos 6 caracteres";
        }
        return null;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "email='" + email + '\'' +
                ", password='[PROTECTED]'" +
                '}';
    }
}