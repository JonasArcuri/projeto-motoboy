package com.example.projeto_motoboy.controller;

import com.example.projeto_motoboy.dto.AuthResponse;
import com.example.projeto_motoboy.dto.LoginRequest;
import com.example.projeto_motoboy.dto.ResetPasswordRequest;
import com.example.projeto_motoboy.model.User;
import com.example.projeto_motoboy.repository.EstabelecimentoRepository;
import com.example.projeto_motoboy.repository.UserRepository;
import com.example.projeto_motoboy.service.EmailService;
import com.example.projeto_motoboy.service.PasswordResetService;
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
    private EstabelecimentoRepository estabelecimentoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordResetService passwordResetService;

    // Endpoint para solicitar reset (envia email com token)
    @PostMapping("/request-reset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody ResetPasswordRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email é obrigatório");
        }

        String frontendResetUrl = "http://localhost:5173/reset/confirm?token=";
        passwordResetService.createPasswordResetTokenAndSendEmail(request.getEmail().trim(), frontendResetUrl);

        return ResponseEntity.ok("Se o email estiver cadastrado, você receberá instruções para reset.");
    }

    // Endpoint para efetuar reset de senha
    @PostMapping("/reset")
    public ResponseEntity<?> doPasswordReset(@RequestBody ResetPasswordRequest request) {
        String token = request.getToken();
        String newPassword = request.getNewPassword();

        if (token == null || token.isBlank() || newPassword == null || newPassword.length() < 6) {
            return ResponseEntity.badRequest().body("Token ou nova senha inválidos.");
        }

        boolean ok = passwordResetService.resetPassword(token.trim(), newPassword);
        if (ok) {
            return ResponseEntity.ok("Senha alterada com sucesso.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token inválido ou expirado.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Tentativa de login para email: " + loginRequest.getEmail());

            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Email é obrigatório");
            }

            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Senha é obrigatória");
            }

            User user = userRepository.findByEmail(loginRequest.getEmail().trim());

            if (user == null) {
                System.out.println("Usuário não encontrado: " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Credenciais inválidas");
            }

            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                System.out.println("Senha incorreta para usuário: " + loginRequest.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Credenciais inválidas");
            }

            System.out.println("Login bem-sucedido para: " + loginRequest.getEmail());

            // Se for estabelecimento, buscar o id do estabelecimento
            Long estabelecimentoId = null;
            if ("Estabelecimento".equalsIgnoreCase(user.getTipo())) {
                var estabelecimento = estabelecimentoRepository.findByUserId(user.getId());
                if (estabelecimento.isPresent()) {
                    estabelecimentoId = estabelecimento.get().getId();
                }
            }

            // Criar resposta incluindo o estabelecimentoId
            AuthResponse authResponse = new AuthResponse(
                    user.getId(),
                    user.getName(),
                    user.getEmail(),
                    user.getTipo(),
                    "Login realizado com sucesso",
                    estabelecimentoId // 🔹 agora vai no retorno
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
