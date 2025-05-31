package br.com.sistemacadastro.sistemacadastro.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Contrato;
import br.com.sistemacadastro.sistemacadastro.model.Escalas;
import br.com.sistemacadastro.sistemacadastro.model.Setores;
import br.com.sistemacadastro.sistemacadastro.model.Turnos;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.EscalaRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SetoresRepository;
@Service
public class EscalaService {

    @Autowired
    private RegrasCLTService regrasCLTService;

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private EscalaRepository escalaRepository;

    public List<Escalas> gerarEscalasSemanais() {
        List<Escalas> escalasGeradas = new ArrayList<>();

        List<Setores> setores = setoresRepository.findAll();

        LocalDate hoje = LocalDate.now();
        LocalDate domingo = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY)); // Começa no domingo da semana atual

        for (Setores setor : setores) {
            List<Colaborador> colaboradoresAtivos = colaboradorRepository.findBySetorAndContratoAtivo(setor.getId());

            for (Colaborador colaborador : colaboradoresAtivos) {
                Contrato contrato = colaborador.getContrato();
                Cargos cargo = contrato.getCargos();

                List<Turnos> turnosDisponiveis = colaborador.getTurnos();
                if (turnosDisponiveis.isEmpty()) continue;

                for (int i = 0; i < 7; i++) {
                    LocalDate dataEscala = domingo.plusDays(i); // Vai de domingo a sábado
                    DayOfWeek diaSemana = dataEscala.getDayOfWeek();

                    boolean ehFolga = contrato.getDiasFolga() != null &&
                            contrato.getDiasFolga().stream()
                                    .anyMatch(d -> DayOfWeek.valueOf(d.name()).equals(diaSemana));
                    if (ehFolga) continue;

                    Turnos turnoParaDia = turnosDisponiveis.get(i % turnosDisponiveis.size());

                    Escalas escalaTemp = new Escalas();
                    escalaTemp.setColaborador(colaborador);
                    escalaTemp.setTurnos(turnoParaDia);
                    escalaTemp.setDataEscala(Date.from(dataEscala.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    escalaTemp.setSetores(setor);

                    List<Escalas> escalasDoColaboradorNaSemana = escalaRepository.findByColaboradorAndSemana(colaborador.getId(), hoje);

                    List<Escalas> escalasParaValidar = new ArrayList<>(escalasDoColaboradorNaSemana);
                    escalasParaValidar.add(escalaTemp);

                    boolean pode = regrasCLTService.podeEscalar(contrato, cargo, escalasParaValidar);

                    if (pode) {
                        Escalas escalaSalva = escalaRepository.save(escalaTemp);
                        escalasGeradas.add(escalaSalva);
                    }
                }
            }
        }

        escalaRepository.saveAll(escalasGeradas);
        return escalasGeradas;
    }
}
