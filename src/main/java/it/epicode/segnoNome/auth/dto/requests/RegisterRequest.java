package it.epicode.segnoNome.auth.dto.requests;

import lombok.Data;

@Data
public class RegisterRequest {
    private String username;
    private String password;
}
