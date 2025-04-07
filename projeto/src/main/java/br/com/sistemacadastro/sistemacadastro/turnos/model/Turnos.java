package br.com.sistemacadastro.sistemacadastro.turnos.model;

import br.com.sistemacadastro.sistemacadastro.collaborator.model.Collaborator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Time;
import java.util.List;

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
