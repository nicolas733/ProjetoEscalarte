package br.com.sistemacadastro.sistemacadastro.Setores.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SetoresDto {

    @NotBlank(message = "O nome é obrigatorio")
    private String nomeSetor;

    @NotBlank(message = "A quantidade é obrigatorio")
    private int quantidadeColaboradores;
}
