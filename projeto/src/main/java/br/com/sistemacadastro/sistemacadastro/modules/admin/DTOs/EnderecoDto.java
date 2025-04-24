package br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class EnderecoDto {
    @NotBlank(message = "O bairro deve ser obrigatorio")
    private String bairro;

    @NotBlank(message = "A rua deve ser obrigatoria")
    private String rua;

    @NotBlank(message = "O cep deve ser obrigatorio")
    private String cep;

    @NotBlank(message = "O complemento deve ser obrigatorio")
    private String complemento;

    @NotNull(message = "É obrigatorio o numero")
    private int numero;
}
