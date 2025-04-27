package br.com.sistemacadastro.sistemacadastro.modules.admin.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

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

    @OneToMany(mappedBy = "setor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CargosPorSetor> cargosPorSetor;

}
