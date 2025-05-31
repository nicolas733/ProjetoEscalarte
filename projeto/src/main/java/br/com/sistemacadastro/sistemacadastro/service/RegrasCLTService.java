package br.com.sistemacadastro.sistemacadastro.service;

import br.com.sistemacadastro.sistemacadastro.model.*;
import br.com.sistemacadastro.sistemacadastro.repository.EscalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
@Service
public class RegrasCLTService {

    public boolean podeEscalar(Contrato contrato, Cargos cargo, List<Escalas> escalas) {
        for (Escalas escala : escalas) {
            if (calculaCargaHorariaDiaria(escala.getTurnos()) > contrato.getCargaHorariaDiaria()) return false;
        }

        if (!validaIntervaloInterjornada(escalas, cargo.getIntervaloInterjornada())) return false;

        int cargaSemanal = calculaCargaHorariaSemanal(escalas);
        if (cargaSemanal > contrato.getDiasTrabalhadosSemanal() * contrato.getCargaHorariaDiaria()) return false;

        int cargaMensal = calculaCargaHorariaMensal(escalas);
        if (cargaMensal > contrato.getDiasTrabalhadosMensal() * contrato.getCargaHorariaDiaria()) return false;

        if (cargo.getCargaHorarioLimite() > 0 && cargaSemanal > cargo.getCargaHorarioLimite()) return false;

        return true;
    }

    private int calculaCargaHorariaDiaria(Turnos turno) {
        long horas = Duration.between(turno.getHorarioInicio(), turno.getHorarioFim()).toHours();
        return (int) (horas <= 0 ? horas + 24 : horas);
    }

    private boolean validaIntervaloInterjornada(List<Escalas> escalas, int intervaloMinimo) {
        escalas.sort(Comparator.comparing(Escalas::getDataEscala));
        for (int i = 0; i < escalas.size() - 1; i++) {
            Escalas atual = escalas.get(i);
            Escalas proximo = escalas.get(i + 1);

            long intervalo = calculaIntervaloEntreTurnos(
                    atual.getDataEscala(), atual.getTurnos().getHorarioFim(),
                    proximo.getDataEscala(), proximo.getTurnos().getHorarioInicio());

            if (intervalo < intervaloMinimo) return false;
        }
        return true;
    }

    private long calculaIntervaloEntreTurnos(Date dataFim, LocalTime fim, Date dataInicio, LocalTime inicio) {
        LocalDateTime fimTurno = LocalDateTime.ofInstant(dataFim.toInstant(), ZoneId.systemDefault())
                .with(fim);
        LocalDateTime inicioTurno = LocalDateTime.ofInstant(dataInicio.toInstant(), ZoneId.systemDefault())
                .with(inicio);

        if (!inicioTurno.isAfter(fimTurno)) inicioTurno = inicioTurno.plusDays(1);

        return Duration.between(fimTurno, inicioTurno).toHours();
    }

    private int calculaCargaHorariaSemanal(List<Escalas> escalas) {
        return escalas.stream()
                .mapToInt(e -> calculaCargaHorariaDiaria(e.getTurnos()))
                .sum();
    }

    private int calculaCargaHorariaMensal(List<Escalas> escalas) {
        return escalas.stream()
                .mapToInt(e -> calculaCargaHorariaDiaria(e.getTurnos()))
                .sum();
    }
}
