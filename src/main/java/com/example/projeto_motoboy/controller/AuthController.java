package com.example.projeto_motoboy.controller;

import com.example.projeto_motoboy.dto.AuthResponse;
import com.example.projeto_motoboy.dto.LoginRequest;
import com.example.projeto_motoboy.model.User;
import com.example.projeto_motoboy.repository.UserRepository;
import com.example.projeto_motoboy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173"}) // Múltiplas origens
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Tentativa de login para email: " + loginRequest.getEmail());

            // Validações básicas
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email é obrigatório");
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Senha é obrigatória");
            }

            // Buscar usuário por email
            User user = userRepository.findByEmail(loginRequest.getEmail().trim());

            if (user == null) {
                System.out.println("Usuário não encontrado: " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Credenciais inválidas");
            }

            // Verificar senha
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                System.out.println("Senha incorreta para usuário: " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Credenciais inválidas");
            }

            System.out.println("Login bem-sucedido para: " + loginRequest.getEmail());

            // Criar resposta de sucesso
            AuthResponse authResponse = new AuthResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getTipo(),
                    "Login realizado com sucesso"
            );

            return ResponseEntity.ok(authResponse);

        } catch (Exception e) {
            System.err.println("Erro interno no login: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro interno do servidor");
        }
    }
}