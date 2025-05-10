package br.com.sistemacadastro.sistemacadastro.modules.admin.cargosPorSetor;

import br.com.sistemacadastro.sistemacadastro.modules.admin.cargo.Cargos;
import br.com.sistemacadastro.sistemacadastro.modules.admin.setor.Setores;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity(name = "cargoSetor")
public class CargosPorSetor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    //um cargoSetor para um cargo
    //relaciona e colaca um cargo_id da table Cargos nessa tabela
    @OneToOne
    @JoinColumn(name = "cargo_id")
    private Cargos cargo;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "cargo_id", insertable = false, updatable = false)
    private int cargoId;

    //Muitos cargoSetor para um setor
    //relaciona as tabelas
    @ManyToOne
    @JoinColumn(name = "setores_id")
    private Setores setor;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "setores_id", insertable = false, updatable = false)
    private int setorId;

}

