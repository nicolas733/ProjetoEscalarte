package br.com.sistemacadastro.sistemacadastro.escalas.model;

import br.com.sistemacadastro.sistemacadastro.collaborator.model.Collaborator;
import br.com.sistemacadastro.sistemacadastro.turnos.model.Turnos;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Escalas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private Date dataEscala;

    @OneToOne
    @JoinColumn(name = "turno_id", nullable = false, unique = true)
    private Turnos turnos;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "turno_id", insertable = false, updatable = false)
    private int turnoId;

    @OneToOne
    @JoinColumn(name = "collaborator_id", nullable = false, unique = true)
    private Collaborator collaborator;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "collaborator_id", insertable = false, updatable = false)
    private int collaboratorId;

}
