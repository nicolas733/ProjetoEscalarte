package br.com.sistemacadastro.sistemacadastro.modules.admin.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity(name= "contrato")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    boolean ativo;

    @NotNull
    private int cargaHorariaDiaria;

    @NotNull
    private int diasTrabalhadosSemanal;

    @NotNull
    private int diasTrabalhadosMensal;

    @CreationTimestamp
    private LocalDate createdAt;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dataFimContrato;

    @OneToOne()
    @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "collaborator_id", insertable = false, updatable = false)
    private int collaboratorId;

    @OneToOne()
    @JoinColumn(name = "cargos_id")
    private Cargos cargos;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "cargos_id", insertable = false, updatable = false)
    private int cargosId;

}
