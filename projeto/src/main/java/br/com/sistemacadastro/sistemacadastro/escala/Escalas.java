package br.com.sistemacadastro.sistemacadastro.escala;

import br.com.sistemacadastro.sistemacadastro.colaborador.Colaborador;
import br.com.sistemacadastro.sistemacadastro.turno.turnos;
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
    private turnos.Turnos turnos;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "turno_id", insertable = false, updatable = false)
    private int turnoId;

    @OneToOne
    @JoinColumn(name = "collaborator_id", nullable = false, unique = true)
    private Colaborador colaborador;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "collaborator_id", insertable = false, updatable = false)
    private int collaboratorId;

}
