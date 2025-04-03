/*
package br.com.sistemacadastro.sistemacadastro.Setores.model;

import br.com.sistemacadastro.sistemacadastro.collaborator.model.Collaborator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank
    private int quantidadeColaboradores;

    @NotBlank
    private String cargosetor;

    @OneToMany(mappedBy = "setores", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Collaborator> collaborators;

    @OneToOne
    @JoinColumn(name = "collaborator_id", referencedColumnName = "id", unique = true)
    private Collaborator collaborator;
}

 */

