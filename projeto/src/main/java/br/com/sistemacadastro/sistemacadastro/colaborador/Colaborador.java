package br.com.sistemacadastro.sistemacadastro.colaborador;

import br.com.sistemacadastro.sistemacadastro.contrato.Contrato;
import br.com.sistemacadastro.sistemacadastro.endereco.Endereco;
import br.com.sistemacadastro.sistemacadastro.escala.Escalas;
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

@Data
@ToString(exclude = "contrato")  // Exclui o campo 'contrato' do método toString()
@Entity(name="colaborador")
public class Colaborador {

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

    @Column(name = "escalas_id", updatable = false, insertable = false)
    private Integer escalasId;


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

    @Column(name = "endereco_id", updatable = false, insertable = false)
    private Integer enderecoId;

}

