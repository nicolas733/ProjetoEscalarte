package br.com.sistemacadastro.sistemacadastro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;

@Data
@Entity(name="turnos")
public class Turnos {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @NotBlank
        private String nome;

        @NotNull
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime horarioInicio;

        @NotNull
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime horarioFim;

        @JsonIgnore
        @ManyToMany(mappedBy = "turnos")
        private List<Colaborador> colaboradores;



}
