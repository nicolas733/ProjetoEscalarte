package br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador;

import br.com.sistemacadastro.sistemacadastro.modules.admin.contrato.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.admin.endereco.Endereco;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CollaboratorDto {

    private int id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    private String email;

    @NotBlank(message = "A senha é obrigatório")
    private String senha;

    @NotBlank(message = "O cpf é obrigatorio")
    private String cpf;

    @NotNull
    private Collaborator.UserType userType;

    @NotBlank(message = "O telefone é obrigatorio")
    private String telefone;

    @NotNull(message = "A data de nascimento é obrigatorio")
    private LocalDate dataNascimento;

    @NotNull(message = "O endereço é obrigatorio")
    private Endereco endereco;

    @NotNull
    private Contrato contrato;

    @NotNull
    private Integer cargoId;


}
