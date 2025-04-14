package br.com.sistemacadastro.sistemacadastro.modules.collaborator.model;

import br.com.sistemacadastro.sistemacadastro.modules.contrato.model.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.endereco.model.Endereco;
import br.com.sistemacadastro.sistemacadastro.modules.escalas.model.Escalas;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@Entity(name="collaborator")
public class Collaborator {
    public enum UserType {
        ADMIN,
        GERENTE,
        OPERADOR
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty()
    private String nome;

    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    private String senha;

    @CreationTimestamp
    private LocalDate createdAt;

    @NotNull
    private UserType userType;

    @NotEmpty
    private String cpf;

    @NotEmpty
    private String telefone;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date dataNascimento;


    //Um colaborador em uma escala
    // esta fazendo um relacionamento e incluindo escalas_id na table de Collaborator
    @OneToOne
    @JoinColumn(name = "escalas_id")
    private Escalas escalas;

    @Column(name = "escalas_id", updatable = false, insertable = false)
    private Integer escalasId;


    //Um contrato para um colaborador
    //o join esta relacionandp as duas tabelas e dizendo q nessa tabela vai receber uma fk de contrato
    @OneToOne
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;

    @Column(name = "contrato_id", updatable = false, insertable = false)
    private Integer contratoId;

    //Um colaborador possui um endereço e um endereço possui um colaborador
    //JoinCollumn esta relacionando com a table endereço e recebendo nessa table um FK
    @OneToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @Column(name = "endereco_id", updatable = false, insertable = false)
    private Integer enderecoId;

}

