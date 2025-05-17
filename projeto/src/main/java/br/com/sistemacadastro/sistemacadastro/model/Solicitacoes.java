package br.com.sistemacadastro.sistemacadastro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity(name = "solicitacoes")
public class Solicitacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private LocalDate dataSolicitacao;

    @NotBlank
    private String descricaoSolicitacao;

    @ManyToOne
    @JoinColumn(name = "colaborador_id")
    private Colaborador colaborador;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "colaborador_id", insertable = false, updatable = false)
    private Integer collaboratorId;

    @OneToOne
    @JoinColumn(name = "escalas_id")
    private Escalas escalas;


    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "escalas_id", insertable = false, updatable = false)
    private Integer escalasId;
}
