package br.com.sistemacadastro.sistemacadastro.service;

import br.com.sistemacadastro.sistemacadastro.model.*;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class RegrasCLTService {

    public boolean podeEscalar(Contrato contrato, Cargos cargo, List<Escalas> escalas) {
        for (Escalas escala : escalas) {
            int cargaDiaria = calculaCargaHorariaDiaria(escala.getTurnos());
            System.out.println("Escala dia " + escala.getDataEscala() + " - Carga diária: " + cargaDiaria);
            if (cargaDiaria > contrato.getCargaHorariaDiaria()) {
                System.out.println("Carga diária maior que a contratada. Escala inválida.");
                return false;
            }
        }

        if (!validaIntervaloInterjornada(escalas, cargo.getIntervaloInterjornada())) {
            System.out.println("Intervalo interjornada inválido.");
            return false;
        }

        int cargaSemanal = calculaCargaHorariaSemanal(escalas);
        int limiteSemanal = contrato.getDiasTrabalhadosSemanal() * contrato.getCargaHorariaDiaria();
        System.out.println("Carga semanal: " + cargaSemanal + " - Limite semanal: " + limiteSemanal);
        if (cargaSemanal > limiteSemanal) {
            System.out.println("Carga semanal excede limite contratual.");
            return false;
        }

        int cargaMensal = calculaCargaHorariaMensal(escalas);
        int limiteMensal = contrato.getDiasTrabalhadosMensal() * contrato.getCargaHorariaDiaria();
        System.out.println("Carga mensal: " + cargaMensal + " - Limite mensal: " + limiteMensal);
        if (cargaMensal > limiteMensal) {
            System.out.println("Carga mensal excede limite contratual.");
            return false;
        }

        if (cargo.getCargaHorarioLimite() > 0 && cargaSemanal > cargo.getCargaHorarioLimite()) {
            System.out.println("Carga semanal excede limite do cargo.");
            return false;
        }

        return true;
    }

    private int calculaCargaHorariaDiaria(Turnos turno) {
        if (turno == null || turno.getHorarioInicio() == null || turno.getHorarioFim() == null) {
            System.out.println("Turno ou horários inválidos: " + turno);
            return 0;
        }
        long horas = Duration.between(turno.getHorarioInicio(), turno.getHorarioFim()).toHours();
        int carga = (int) (horas <= 0 ? horas + 24 : horas);
        System.out.println("Turno: " + turno + " - Carga horária calculada: " + carga);
        return carga;
    }

    private boolean validaIntervaloInterjornada(List<Escalas> escalas, int intervaloMinimo) {
        escalas.sort(Comparator.comparing(Escalas::getDataEscala));
        for (int i = 0; i < escalas.size() - 1; i++) {
            Escalas atual = escalas.get(i);
            Escalas proximo = escalas.get(i + 1);

            long diasEntre = ChronoUnit.DAYS.between(
                    atual.getDataEscala().toInstant().atZone(ZoneId.systemDefault()).toLocalDate(),
                    proximo.getDataEscala().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            );

            if (diasEntre > 1) continue; // ignora se os dias não são consecutivos

            long intervalo = calculaIntervaloEntreTurnos(
                    atual.getDataEscala(), atual.getTurnos().getHorarioFim(),
                    proximo.getDataEscala(), proximo.getTurnos().getHorarioInicio());

            System.out.println("Intervalo entre turnos (" + atual.getDataEscala() + " e " + proximo.getDataEscala() + "): " + intervalo + " horas");

            if (intervalo < intervaloMinimo) {
                System.out.println("Intervalo entre turnos menor que o mínimo requerido: " + intervaloMinimo);
                return false;
            }
        }
        return true;
    }

    private long calculaIntervaloEntreTurnos(Date dataFim, LocalTime fim, Date dataInicio, LocalTime inicio) {
        LocalDateTime fimTurno = LocalDateTime.ofInstant(dataFim.toInstant(), ZoneId.systemDefault()).with(fim);
        LocalDateTime inicioTurno = LocalDateTime.ofInstant(dataInicio.toInstant(), ZoneId.systemDefault()).with(inicio);

        if (!inicioTurno.isAfter(fimTurno)) inicioTurno = inicioTurno.plusDays(1);

        long intervalo = Duration.between(fimTurno, inicioTurno).toHours();
        return intervalo;
    }

    private int calculaCargaHorariaSemanal(List<Escalas> escalas) {
        if (escalas.isEmpty()) return 0;

        LocalDate semanaRef = escalas.get(0).getDataEscala().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int semanaDoAno = semanaRef.get(ChronoField.ALIGNED_WEEK_OF_YEAR);
        int ano = semanaRef.getYear();

        int cargaSemanal = escalas.stream()
                .filter(e -> {
                    LocalDate data = e.getDataEscala().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return data.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == semanaDoAno && data.getYear() == ano;
                })
                .mapToInt(e -> calculaCargaHorariaDiaria(e.getTurnos()))
                .sum();

        System.out.println("Carga horária semanal calculada: " + cargaSemanal);
        return cargaSemanal;
    }

    private int calculaCargaHorariaMensal(List<Escalas> escalas) {
        int cargaMensal = escalas.stream()
                .mapToInt(e -> calculaCargaHorariaDiaria(e.getTurnos()))
                .sum();

        System.out.println("Carga horária mensal calculada: " + cargaMensal);
        return cargaMensal;
    }
}
