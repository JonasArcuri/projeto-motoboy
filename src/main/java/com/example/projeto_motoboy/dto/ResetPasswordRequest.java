package com.example.projeto_motoboy.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ResetPasswordRequest {
    private String email;
    private String token;
    private String newPassword;
}
