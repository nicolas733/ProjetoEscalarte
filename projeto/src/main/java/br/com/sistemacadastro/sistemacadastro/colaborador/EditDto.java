package br.com.sistemacadastro.sistemacadastro.colaborador;

import br.com.sistemacadastro.sistemacadastro.contrato.Contrato;
import br.com.sistemacadastro.sistemacadastro.endereco.Endereco;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

@Data
public class EditDto {

    private int id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(min = 4, message = "O nome deve ter no mínimo 4 letras")
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
    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}[- ]?\\d{4}$",message = "Telefone inválido. Use formatos como (XX) XXXXX-XXXX ou XX XXXXX XXXX")
    private String telefone;


    @Valid
    @NotNull(message = "O endereço é obrigatorio")
    private Endereco endereco;

    @NotNull
    private Contrato contrato;


}