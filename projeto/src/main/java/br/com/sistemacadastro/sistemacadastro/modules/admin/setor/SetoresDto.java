package br.com.sistemacadastro.sistemacadastro.modules.admin.setor;

import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.Colaborador;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SetoresDto {

    private Integer id;

    @NotBlank(message = "O nome é obrigatorio")
    @Pattern(regexp = "^[A-Za-zÀ-ú ]+$", message = "O nome deve conter apenas letras e espaços")
    @Size(min = 4, message = "O nome deve ter no mínimo 4 letras")
    private String nomeSetor;

    private Colaborador gerenteSetor;

    @NotNull(message = "A quantidade é obrigatorio")
    @Min(value = 1, message = "Minimo 1 colaborador")
    private Integer quantidadeColaboradores;
}
