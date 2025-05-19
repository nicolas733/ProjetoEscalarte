package br.com.sistemacadastro.sistemacadastro.dto;

import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Contrato;
import br.com.sistemacadastro.sistemacadastro.model.Endereco;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import lombok.Data;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;

@Data
public class ColaboradorDTO {

    private int id;

    @NotBlank(message = "O nome é obrigatório")
    @Pattern(regexp = "^[A-Za-zÀ-ú ]+$", message = "O nome deve conter apenas letras e espaços")
    @Size(min = 4, message = "O nome deve ter no mínimo 4 letras")
    private String nome;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "Email invalido")
    private String email;

    @NotBlank(message = "A senha é obrigatório")
    @Size(min = 4, message = "A senha deve ter no mínimo 4 caracteres")
    private String senha;

    @NotBlank(message = "O cpf é obrigatorio")
    @CPF(message = "CPF invalido")
    private String cpf;

    @NotNull
    private Colaborador.TipoUsuario tipoUsuario;

    @NotBlank(message = "O telefone é obrigatorio")
    @Pattern(regexp = "^\\(?\\d{2}\\)?\\s?\\d{4,5}[- ]?\\d{4}$",message = "Telefone inválido. Use formatos como (XX) XXXXX-XXXX ou XX XXXXX XXXX")
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
