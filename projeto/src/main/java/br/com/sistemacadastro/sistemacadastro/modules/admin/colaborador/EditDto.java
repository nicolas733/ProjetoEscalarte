package br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador;

import br.com.sistemacadastro.sistemacadastro.modules.admin.contrato.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.admin.endereco.Endereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class EditDto {

    private int id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email invalido")
    private String email;


    @NotBlank(message = "O cpf é obrigatorio")
    @CPF(message = "CPF invalido")
    private String cpf;

    @NotNull
    private Colaborador.TipoUsuario tipoUsuario;

    @NotBlank(message = "O telefone é obrigatorio")
    private String telefone;


    @Valid
    @NotNull(message = "O endereço é obrigatorio")
    private Endereco endereco;

    @NotNull
    private Contrato contrato;


}