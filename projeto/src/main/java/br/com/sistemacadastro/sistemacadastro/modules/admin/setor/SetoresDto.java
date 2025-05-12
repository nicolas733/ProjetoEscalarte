package br.com.sistemacadastro.sistemacadastro.modules.admin.setor;

import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.Colaborador;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SetoresDto {

    @NotBlank(message = "O nome é obrigatorio")
    private String nomeSetor;

    private Colaborador gerenteSetor;

    @NotNull(message = "A quantidade é obrigatorio")
    private int quantidadeColaboradores;
}
