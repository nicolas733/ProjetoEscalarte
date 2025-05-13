package br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador;

import br.com.sistemacadastro.sistemacadastro.modules.admin.contrato.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.admin.endereco.Endereco;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

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
    private Colaborador.TipoUsuario tipoUsuario;

    @NotBlank(message = "O telefone é obrigatorio")
    private String telefone;


    @NotNull(message = "O endereço é obrigatorio")
    private Endereco endereco;

    @NotNull
    private Contrato contrato;


}