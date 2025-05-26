package br.com.sistemacadastro.sistemacadastro.dto;

import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Escalas;
import br.com.sistemacadastro.sistemacadastro.model.Turnos;
import lombok.Data;

import java.time.LocalTime;
import java.util.Date;


public class EscalaDTO {

    private String nomeColaborador;
    private String cargo;
    private String turnoDescricao;
    private Date dataEscala;

    private Colaborador colaborador;
    private Turnos turno;

    public EscalaDTO(Colaborador colaborador, Turnos turno, Date dataEscala) {
        this.nomeColaborador = colaborador.getNome();
        this.cargo = colaborador.getContrato().getCargos().getNomeCargo() != null ? colaborador.getContrato().getCargos().getNomeCargo() : "Sem cargo";
        this.turnoDescricao = turno.getNome();
        this.dataEscala = dataEscala;
        this.colaborador = colaborador;
        this.turno = turno;
    }

    // Método para converter DTO em entidade Escala
    public Escalas toEntity() {
        Escalas escala = new Escalas();
        escala.setColaborador(this.colaborador);
        escala.setTurnos(this.turno);
        escala.setDataEscala(this.dataEscala);
        return escala;
    }

    // Getters e Setters
    public String getNomeColaborador() {
        return nomeColaborador;
    }

    public void setNomeColaborador(String nomeColaborador) {
        this.nomeColaborador = nomeColaborador;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getTurnoDescricao() {
        return turnoDescricao;
    }

    public void setTurnoDescricao(String turnoDescricao) {
        this.turnoDescricao = turnoDescricao;
    }

    public Date getDataEscala() {
        return dataEscala;
    }

    public void setDataEscala(Date dataEscala) {
        this.dataEscala = dataEscala;
    }

    public Colaborador getColaborador() {
        return colaborador;
    }

    public void setColaborador(Colaborador colaborador) {
        this.colaborador = colaborador;
    }

    public Turnos getTurno() {
        return turno;
    }

    public void setTurno(Turnos turno) {
        this.turno = turno;
    }
}
