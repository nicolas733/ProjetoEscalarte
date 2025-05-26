package br.com.sistemacadastro.sistemacadastro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Data
@ToString(exclude = "contrato")  // Exclui o campo 'contrato' do método toString()
@Entity(name="colaborador")
public class  Colaborador {

    @NotEmpty
    public enum TipoUsuario {
        ADMIN,
        GERENTE,
        OPERADOR,
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty()
    private String nome;

    @NotEmpty
    @Email(message = "Email invalido")
    private String email;

    @NotEmpty
    private String senha;

    @CreationTimestamp
    private LocalDate createdAt;

    @NotNull
    private TipoUsuario tipoUsuario;

    @NotEmpty
    @CPF(message = "CPF invalido")
    private String cpf;

    @NotEmpty
    private String telefone;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @NotNull
    private LocalDate dataNascimento;


    //Um colaborador em uma escala
    // esta fazendo um relacionamento e incluindo escalas_id na table de Collaborator
    @OneToOne
    @JoinColumn(name = "escalas_id")
    private Escalas escalas;



    //Um contrato para um colaborador
    //o join esta relacionandp as duas tabelas e dizendo q nessa tabela vai receber uma fk de contrato
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contrato_id")
    private Contrato contrato;


    //Um colaborador possui um endereço e um endereço possui um colaborador
    //JoinCollumn esta relacionando com a table endereço e recebendo nessa table um FK
    @OneToOne
    @JoinColumn(name = "endereco_id")
    private Endereco endereco;

    @ManyToOne
    @JoinColumn(name = "cargo_por_setor_id")
    private CargosPorSetor cargoPorSetor;


    @Column(name = "endereco_id", updatable = false, insertable = false)
    private Integer enderecoId;

    @ManyToMany
    @JoinTable(
            name = "colaborador_turno",
            joinColumns = @JoinColumn(name = "colaborador_id"),
            inverseJoinColumns = @JoinColumn(name = "turno_id")
    )
    private List<Turnos> turnos;

}

