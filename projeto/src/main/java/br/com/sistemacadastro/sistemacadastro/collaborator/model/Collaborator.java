package br.com.sistemacadastro.sistemacadastro.collaborator.model;


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
/*
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
    @JoinColumn(name = "setores_id", nullable = false)
    private Setores setores;

 */
}

