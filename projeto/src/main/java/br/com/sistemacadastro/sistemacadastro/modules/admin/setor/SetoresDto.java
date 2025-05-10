package br.com.sistemacadastro.sistemacadastro.modules.admin.setor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetoresDto {

    @NotBlank(message = "O nome é obrigatorio")
    private String nomeSetor;

    @NotNull(message = "A quantidade é obrigatorio")
    private int quantidadeColaboradores;
}
