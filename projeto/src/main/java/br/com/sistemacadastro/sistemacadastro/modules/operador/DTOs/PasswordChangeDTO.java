package br.com.sistemacadastro.sistemacadastro.modules.operador.DTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PasswordChangeDTO {
    @NotBlank(message = "Digite seu email")
    private String email;

    @NotBlank(message = "Digite sua senha antiga")
    private String senha; // Senha antiga

    @NotBlank(message = "Digite sua nova senha")
    private String novaSenha;
}
