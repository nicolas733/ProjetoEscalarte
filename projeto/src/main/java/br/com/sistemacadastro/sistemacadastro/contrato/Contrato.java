package br.com.sistemacadastro.sistemacadastro.contrato;

import br.com.sistemacadastro.sistemacadastro.cargo.Cargos;
import br.com.sistemacadastro.sistemacadastro.colaborador.Colaborador;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Data
@ToString(exclude = "colaborador")
@Entity(name= "contrato")
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    boolean ativo = true;

    @NotNull(message = "Deve ser cadastrado")
    private Integer cargaHorariaDiaria;

    @NotNull(message = "Deve ser cadastrado")
    private Integer diasTrabalhadosSemanal;

    @NotNull(message = "Deve ser cadastrado")
    private Integer diasTrabalhadosMensal;

    @NotNull(message = "Deve ser cadastrado")
    @Min(value = 11, message = "O tempo minimo de iterjornada é de 11 horas")
    private Integer intervaloInterjornada;

    @CreationTimestamp
    private LocalDate createdAt;

    @NotNull(message = "Deve ser cadastrado")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date dataFimContrato;

    @OneToOne()
    @JoinColumn(name = "colaborador_id")
    private Colaborador colaborador;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "colaborador_id", insertable = false, updatable = false)
    private int colaboradorId;

    @OneToOne()
    @JoinColumn(name = "cargos_id")
    private Cargos cargos;

    //@Column faz acessar o Id dessa tabela sem acessar a tabela por inteiro
    //insertable  = false, updatable = false diz que essa propriedade é apenas de leitura
    @Column(name = "cargos_id", insertable = false, updatable = false)
    private Integer cargosId;

}
