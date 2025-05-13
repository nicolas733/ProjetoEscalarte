package br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Endereco;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.Date;

@Data
public class CollaboratorDto {

    private int id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email invalido")
    private String email;

    @NotBlank(message = "A senha é obrigatório")
    private String senha;

    @NotBlank(message = "O cpf é obrigatorio")
    @CPF(message = "CPF invalido")
    private String cpf;

    @NotNull
    private Collaborator.UserType userType;

    @NotBlank(message = "O telefone é obrigatorio")
    private String telefone;

    @NotNull(message = "A data de nascimento é obrigatorio")
    private LocalDate dataNascimento;

    @NotNull(message = "O endereço é obrigatorio")
    private Endereco endereco;

    @Valid
    @NotNull
    private Contrato contrato;

    @NotNull
    private Integer cargoId;


}
