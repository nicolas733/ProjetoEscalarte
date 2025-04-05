package br.com.sistemacadastro.sistemacadastro.contrato.model;

import br.com.sistemacadastro.sistemacadastro.cargos.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.collaborator.model.Collaborator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Entity(name= "contrato")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank
    boolean ativo;

    @NotBlank
    String cargaHoraria;

    @OneToOne()
    @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator;

    @Column(name = "collaborator_id", insertable = false, updatable = false)
    private int collaboratorId;

    @OneToOne()
    @JoinColumn(name = "cargos_id")
    private Cargos cargos;

    @Column(name = "cargos_id", insertable = false, updatable = false)
    private int cargosId;

}
