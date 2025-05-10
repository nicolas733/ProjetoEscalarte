package br.com.sistemacadastro.sistemacadastro.modules.admin.cargo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CargosDto {

    private Integer id;

    @NotBlank(message = "O nome é obrigatório")
    private String nomeCargo;

    @NotNull(message = "O email é obrigatório")
    private Integer cargoHorarioLimite;

    @NotNull(message = "O setor é obrigatório")
    private Integer setorId;

    private String nomeSetor; // <-- adiciona o nome do setor


}
