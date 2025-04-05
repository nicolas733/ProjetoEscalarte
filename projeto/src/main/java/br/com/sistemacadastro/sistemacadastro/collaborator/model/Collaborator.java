package br.com.sistemacadastro.sistemacadastro.collaborator.model;

import br.com.sistemacadastro.sistemacadastro.Setores.model.Setores;
import br.com.sistemacadastro.sistemacadastro.cargos.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.endereco.model.Endereco;
import br.com.sistemacadastro.sistemacadastro.escalas.model.Escalas;
import br.com.sistemacadastro.sistemacadastro.turnos.model.Turnos;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity(name="collaborator")
public class Collaborator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String nome;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String senha;

    @CreationTimestamp
    private LocalDate createdAt;

    @NotEmpty
    private String typeuser;

    @NotEmpty
    private String cpf;

    @NotEmpty
    private String telefone;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;

    @ManyToOne
    @JoinColumn(name = "setores_id", nullable = false) // Essa é a FK para o setor
    private Setores setores;

    @OneToOne(mappedBy = "collaborator", cascade = CascadeType.ALL, orphanRemoval = true)
    private Escalas escala;

    @ManyToOne
    @JoinColumn(name = "turno_id", nullable = false)
    private Turnos turno;

    @ManyToOne
    @JoinColumn(name = "cargo_id", nullable = false)
    private Cargos cargo;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "endereco_id", nullable = false, unique = true)
    private Endereco endereco;

}

