package br.com.sistemacadastro.sistemacadastro.collaborator.model;

import br.com.sistemacadastro.sistemacadastro.Setores.model.Setores;
import br.com.sistemacadastro.sistemacadastro.cargos.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.contrato.model.Contrato;
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


    //Um colaborador em uma escala
    // esta fazendo um relacionamento e incluindo escalas_id na table de Collaborator
    @OneToOne
    @JoinColumn(name = "escalas_id")
    private Escalas escalas;

    //Um contrato para um colaborador
    //o join esta relacionandp as duas tabelas e dizendo q nessa tabela vai receber uma fk de contrato
    @OneToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    //Um colaborador possui um endereço e um endereço possui um colaborador
    //JoinCollumn esta relacionando com a table endereço e recebendo nessa table um FK
    @OneToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;


}

