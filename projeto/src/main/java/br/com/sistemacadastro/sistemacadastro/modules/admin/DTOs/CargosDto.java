package br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CargosDto {
    @NotBlank(message = "O nome é obrigatório")
    private String nomeCargo;

    @NotNull(message = "O email é obrigatório")
    private Integer cargoHorarioLimite;

}
