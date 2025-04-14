package br.com.sistemacadastro.sistemacadastro.modules.collaborator.model;

import br.com.sistemacadastro.sistemacadastro.modules.contrato.model.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.endereco.model.Endereco;
import br.com.sistemacadastro.sistemacadastro.modules.endereco.model.EnderecoDto;
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

    @NotBlank(message = "O cpf é obrigatorio")
    private String cpf;

    @NotBlank(message = "O telefone é obrigatorio")
    private String telefone;

    @NotNull(message = "A data de nascimento é obrigatorio")
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @NotNull(message = "O endereço é obrigatorio")
    private Endereco endereco;

    @NotNull
    private Contrato contrato;
}
