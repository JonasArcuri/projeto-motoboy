package com.example.projeto_motoboy.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject("Redefinição de Senha - Projeto Motoboy");

            String htmlContent = """
                <div style="font-family: Arial, sans-serif; max-width: 600px; margin: auto;">
                    <h2 style="color: #333;">Olá!</h2>
                    <p>Você solicitou a redefinição da sua senha. Clique no botão abaixo para continuar:</p>
                    <a href="%s" style="display: inline-block; padding: 12px 20px; 
                       background-color: #FA9D06; color: #fff; text-decoration: none; 
                       border-radius: 5px; font-weight: bold;">Redefinir Senha</a>
                    <p style="margin-top: 20px; font-size: 12px; color: #666;">
                        Se você não fez esta solicitação, ignore este email.
                    </p>
                </div>
                """.formatted(resetLink);

            helper.setText(htmlContent, true);

            mailSender.send(message);

            System.out.println("✅ Email de reset enviado para " + to);
        } catch (MessagingException e) {
            System.err.println("❌ Erro ao enviar email: " + e.getMessage());
            throw new RuntimeException("Erro ao enviar email", e);
        }
    }
}