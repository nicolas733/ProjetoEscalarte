package br.com.sistemacadastro.sistemacadastro.modules.cargos.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CargosDto {
    @NotBlank(message = "O nome é obrigatório")
    private String nomeCargo;

    @NotNull(message = "O email é obrigatório")
    private Integer cargoHorarioLimite;

}
