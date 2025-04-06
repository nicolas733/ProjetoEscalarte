package br.com.sistemacadastro.sistemacadastro.solicitacoes.model;

import br.com.sistemacadastro.sistemacadastro.collaborator.model.Collaborator;
import br.com.sistemacadastro.sistemacadastro.escalas.model.Escalas;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;

@Data
@Entity(name = "solicitacoes")
public class Solicitacoes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    private Date dataSolicitacao;

    @NotBlank
    private String descricaoSolicitacao;

    @ManyToOne
    @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "collaborator_id", insertable = false, updatable = false)
    private int collaboratorId;

    @OneToOne
    @JoinColumn(name = "escalas_id")
    private Escalas escalas;


    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "escalas_id", insertable = false, updatable = false)
    private int escalasId;
}
