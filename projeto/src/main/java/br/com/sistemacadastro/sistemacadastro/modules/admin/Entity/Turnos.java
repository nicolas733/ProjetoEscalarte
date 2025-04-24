package br.com.sistemacadastro.sistemacadastro.modules.admin.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Time;

@Data
@Entity(name="turnos")
public class Turnos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private String nome;

    @NotBlank
    private Time horarioInicio;

    @NotBlank
    private Time horarioFim;

}
