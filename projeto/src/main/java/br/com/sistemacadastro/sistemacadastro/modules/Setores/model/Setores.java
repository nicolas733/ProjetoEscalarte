package br.com.sistemacadastro.sistemacadastro.modules.Setores.model;

import br.com.sistemacadastro.sistemacadastro.modules.collaborator.model.Collaborator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity(name ="setores")
public class Setores {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String nomesetor;

    @NotNull
    private int quantidadeColaboradores;

    @OneToOne
    @JoinColumn(name = "gerente_id", referencedColumnName = "id", unique = true)
    private Collaborator gerenteSetor;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "gerente_id", updatable = false, insertable = false)
    private Integer gerenteId;

}
