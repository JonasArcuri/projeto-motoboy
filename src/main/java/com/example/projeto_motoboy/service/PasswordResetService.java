package com.example.projeto_motoboy.service;

import com.example.projeto_motoboy.model.PasswordResetToken;
import com.example.projeto_motoboy.model.User;
import com.example.projeto_motoboy.repository.PasswordResetTokenRepository;
import com.example.projeto_motoboy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final PasswordResetTokenRepository tokenRepo;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordResetService(PasswordResetTokenRepository tokenRepo,
                                UserRepository userRepository,
                                EmailService emailService,
                                PasswordEncoder passwordEncoder) {
        this.tokenRepo = tokenRepo;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    public void createPasswordResetTokenAndSendEmail(String email, String frontEndResetUrl) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // Não vaza informação: apenas retorna sem erro
            return;
        }

        // gerar token único
        String token = UUID.randomUUID().toString();

        PasswordResetToken prt = new PasswordResetToken();
        prt.setToken(token);
        prt.setUser(user);
        prt.setExpiryDate(LocalDateTime.now().plusHours(1)); // expira em 1h
        tokenRepo.save(prt);

        // frontendResetUrl já deve vir com "?token=" no final
        String resetLink = frontEndResetUrl + token;

        // enviar email real (ou mock)
        emailService.sendPasswordResetEmail(user.getEmail(), resetLink);
    }

    @Transactional
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> maybe = tokenRepo.findByToken(token);
        if (maybe.isEmpty()) return false;

        PasswordResetToken prt = maybe.get();
        if (prt.isExpired()) {
            tokenRepo.delete(prt);
            return false;
        }

        User user = prt.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // remover token após uso
        tokenRepo.delete(prt);
        return true;
    }
}
