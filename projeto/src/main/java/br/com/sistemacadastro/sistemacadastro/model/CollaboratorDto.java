package br.com.sistemacadastro.sistemacadastro.model;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CollaboratorDto {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    private String email;

    @NotBlank(message = "A senha é obrigatório")
    private String senha;


}
