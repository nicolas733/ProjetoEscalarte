package br.com.sistemacadastro.sistemacadastro.modules.admin.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity(name="cargos")
public class Cargos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String nomeCargo;

    @NotNull
    private int  cargaHorarioLimite;
}

