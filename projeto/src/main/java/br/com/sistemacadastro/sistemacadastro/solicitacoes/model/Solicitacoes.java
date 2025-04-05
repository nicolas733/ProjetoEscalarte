package br.com.sistemacadastro.sistemacadastro.solicitacoes.model;

import br.com.sistemacadastro.sistemacadastro.collaborator.model.Collaborator;
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

    @ManyToOne
    @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator;

    @Column(name = "collaborator_id", insertable = false, updatable = false)
    private int collaboratorId;
}
