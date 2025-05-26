package br.com.sistemacadastro.sistemacadastro.model;

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
    @JoinColumn(name = "colaborador_id", nullable = false, unique = true)
    private Colaborador colaborador;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "colaborador_id", insertable = false, updatable = false)
    private int colaboradorId;

    @ManyToOne
    @JoinColumn(name = "setor_id")
    private Setores setores;

}
