package br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class EditDto {

    private int id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    private String email;


    @NotBlank(message = "O cpf é obrigatorio")
    private String cpf;

    @NotNull
    private Collaborator.UserType userType;

    @NotBlank(message = "O telefone é obrigatorio")
    private String telefone;


    @NotNull(message = "O endereço é obrigatorio")
    private Endereco endereco;

    @NotNull
    private Contrato contrato;


}