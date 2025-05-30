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
        // Validar carga horária diária
        for (Escalas escala : escalas) {
            int cargaDiaria = calculaCargaHorariaDiaria(escala.getTurnos());
            if (cargaDiaria > contrato.getCargaHorariaDiaria()) {
                return false; // Excede carga diária permitida
            }
        }

        // Validar intervalo interjornada
        if (!validaIntervaloInterjornada(escalas, contrato.getIntervaloInterjornada())) {
            return false; // Intervalo menor que o permitido
        }

        // Validar carga semanal e mensal (simplificado)
        int cargaSemanal = calculaCargaHorariaSemanal(escalas);
        if (cargaSemanal > contrato.getDiasTrabalhadosSemanal() * contrato.getCargaHorariaDiaria()) {
            return false;
        }

        int cargaMensal = calculaCargaHorariaMensal(escalas);
        if (cargaMensal > contrato.getDiasTrabalhadosMensal() * contrato.getCargaHorariaDiaria()) {
            return false;
        }

        // Validar limite do cargo
        if (cargo.getCargaHorarioLimite() > 0 && cargaSemanal > cargo.getCargaHorarioLimite()) {
            return false;
        }

        return true;
    }

    /**
     * Calcula a carga horária diária de um turno, em horas.
     */
    private int calculaCargaHorariaDiaria(Turnos turno) {
        // Se o horário fim for antes do início, assume que o turno passa da meia-noite
        long duracaoHoras = Duration.between(turno.getHorarioInicio(), turno.getHorarioFim()).toHours();
        if (duracaoHoras <= 0) {
            duracaoHoras += 24;
        }
        return (int) duracaoHoras;
    }

    /**
     * Valida se o intervalo entre o término de um turno e o início do próximo é >= intervalo mínimo.
     */
    private boolean validaIntervaloInterjornada(List<Escalas> escalas, int intervaloMinimo) {
        // Ordena escalas por data da escala (dataEscala)
        escalas.sort(Comparator.comparing(Escalas::getDataEscala));

        for (int i = 0; i < escalas.size() - 1; i++) {
            Escalas atual = escalas.get(i);
            Escalas proximo = escalas.get(i + 1);

            LocalTime fimAtual = atual.getTurnos().getHorarioFim();
            LocalTime inicioProximo = proximo.getTurnos().getHorarioInicio();

            // Calcula o intervalo em horas entre o fim do turno atual e início do próximo
            long intervaloHoras = calculaIntervaloEntreTurnos(atual.getDataEscala(), fimAtual, proximo.getDataEscala(), inicioProximo);

            if (intervaloHoras < intervaloMinimo) {
                return false; // Intervalo interjornada menor que permitido
            }
        }
        return true;
    }

    /**
     * Calcula a diferença em horas entre o término de um turno em uma data e o início do próximo turno em outra data.
     * Considera possíveis trocas de dia (exemplo: fim às 23h e início às 07h do dia seguinte).
     */
    private long calculaIntervaloEntreTurnos(Date dataFimTurnoAtual, LocalTime fimAtual, Date dataInicioTurnoProximo, LocalTime inicioProximo) {
        // Converter java.util.Date + LocalTime para LocalDateTime para cálculo
        LocalDateTime fimTurnoAtual = LocalDateTime.ofInstant(dataFimTurnoAtual.toInstant(), ZoneId.systemDefault())
                .withHour(fimAtual.getHour())
                .withMinute(fimAtual.getMinute())
                .withSecond(0).withNano(0);

        LocalDateTime inicioTurnoProximo = LocalDateTime.ofInstant(dataInicioTurnoProximo.toInstant(), ZoneId.systemDefault())
                .withHour(inicioProximo.getHour())
                .withMinute(inicioProximo.getMinute())
                .withSecond(0).withNano(0);

        // Se o início do próximo turno for antes do fim do turno atual, supõe que é no dia seguinte
        if (!inicioTurnoProximo.isAfter(fimTurnoAtual)) {
            inicioTurnoProximo = inicioTurnoProximo.plusDays(1);
        }

        return Duration.between(fimTurnoAtual, inicioTurnoProximo).toHours();
    }

    /**
     * Soma a carga horária semanal (simplificada somando toda carga das escalas).
     */
    private int calculaCargaHorariaSemanal(List<Escalas> escalas) {
        // Aqui você pode implementar agrupamento por semana, mas simplificado:
        return escalas.stream()
                .mapToInt(escala -> calculaCargaHorariaDiaria(escala.getTurnos()))
                .sum();
    }

    /**
     * Soma a carga horária mensal (simplificada somando toda carga das escalas).
     */
    private int calculaCargaHorariaMensal(List<Escalas> escalas) {
        // Similar ao semanal, soma toda a carga horária
        return escalas.stream()
                .mapToInt(escala -> calculaCargaHorariaDiaria(escala.getTurnos()))
                .sum();
    }
}
