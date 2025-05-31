package br.com.sistemacadastro.sistemacadastro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity(name="cargos")
public class Cargos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String nomeCargo;

    @NotNull
    private int cargaHorarioLimite;

    @NotNull(message = "Deve ser cadastrado")
    @Min(value = 11, message = "O tempo minimo de iterjornada é de 11 horas")
    private Integer intervaloInterjornada;


}

