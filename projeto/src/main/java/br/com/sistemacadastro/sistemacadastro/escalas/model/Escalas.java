package br.com.sistemacadastro.sistemacadastro.escalas.model;

import br.com.sistemacadastro.sistemacadastro.collaborator.model.Collaborator;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Escalas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Date dataEscala;

    private String cargaHoraria;

    @OneToOne
    @JoinColumn(name = "collaborator_id", nullable = false, unique = true)
    private Collaborator collaborator;

}
