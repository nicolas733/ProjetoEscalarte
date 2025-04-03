package br.com.sistemacadastro.sistemacadastro.cargos.model;


import br.com.sistemacadastro.sistemacadastro.collaborator.model.Collaborator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity(name="cargos")
public class Cargos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String nomeCargo;

    @NotEmpty
    private String cargahorario;

    @ManyToOne
    @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator;

    @Column(name = "collaborator_id", insertable = false, updatable = false)
    private int collaboratorId;

    // falta setores
}
