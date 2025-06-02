package br.com.sistemacadastro.sistemacadastro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@ToString(exclude = "colaborador")
@EqualsAndHashCode(exclude = "colaborador")
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

    @NotNull(message = "Selecionar dia(s) de folga")
    public enum DiaFolga {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }

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

    @ElementCollection(targetClass = DiaFolga.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "contrato_diafolga", joinColumns = @JoinColumn(name = "contrato_id"))
    @Column(name = "dia_folga")
    private List<DiaFolga> diasFolga;

}
