package br.com.sistemacadastro.sistemacadastro.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CargosDto {

    private Integer id;

    @NotBlank(message = "O nome é obrigatório")
    @Pattern(regexp = "^[A-Za-zÀ-ú ]+$", message = "O nome deve conter apenas letras e espaços")
    @Size(min = 4, message = "O nome deve ter no mínimo 4 letras")
    private String nomeCargo;

    @NotNull(message = "O horario limite deve ser cadastrado")
    @Min(value = 1, message = "A carga horária deve ser maior que zero.")
    private Integer cargoHorarioLimite;

    @NotNull(message = "O setor é obrigatório")
    private Integer setorId;

    private String nomeSetor; // <-- adiciona o nome do setor


}
