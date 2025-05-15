package br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador;

import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.Colaborador;
import br.com.sistemacadastro.sistemacadastro.modules.admin.contrato.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.admin.endereco.Endereco;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
public class ColaboradorDto {

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
    private Colaborador.TipoUsuario tipoUsuario;

    @NotBlank(message = "O telefone é obrigatorio")
    private String telefone;

    @NotNull(message = "A data de nascimento é obrigatorio")
    private LocalDate dataNascimento;

    @Valid
    @NotNull(message = "O endereço é obrigatorio")
    private Endereco endereco;

    @Valid
    @NotNull
    private Contrato contrato;

    @NotNull(message = "Selecione um cargo")
    private Integer cargoId;


}
