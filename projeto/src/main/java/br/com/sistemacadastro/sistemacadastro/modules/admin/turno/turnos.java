package br.com.sistemacadastro.sistemacadastro.modules.admin.turno;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.sql.Time;

public class turnos {
    @Data
    @Entity(name="turnos")
    public static class Turnos {

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
}
