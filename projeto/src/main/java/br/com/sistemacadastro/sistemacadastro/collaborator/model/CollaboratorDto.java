package br.com.sistemacadastro.sistemacadastro.collaborator.model;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class CollaboratorDto {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    private String email;

    @NotBlank(message = "A senha é obrigatório")
    private String senha;

    @NotBlank(message = "Cadastro o tipo de usurio")
    private String typeuser;

    @NotBlank(message = "O cpf é obrigatorio")
    private String cpf;

    @NotBlank(message = "O telefone é obrigatorio")
    private String telefone;

    @NotNull(message = "A data de nascimento é obrigatorio")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;


}
