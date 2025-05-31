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
        LocalDate segunda = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));

        for (Setores setor : setores) {
            List<Colaborador> colaboradores = colaboradorRepository.findBySetorAndContratoAtivo(setor.getId());

            for (Colaborador colaborador : colaboradores) {
                Contrato contrato = colaborador.getContrato();
                Cargos cargo = contrato.getCargos();
                List<Turnos> turnosDisponiveis = colaborador.getTurnos();

                if (turnosDisponiveis.isEmpty()) continue;

                for (int i = 0; i < 7; i++) {
                    LocalDate dataEscala = segunda.plusDays(i);
                    DayOfWeek diaSemana = dataEscala.getDayOfWeek();

                    boolean ehFolga = contrato.getDiasFolga() != null &&
                            contrato.getDiasFolga().stream()
                                    .map(folga -> DayOfWeek.valueOf(folga.name()))
                                    .anyMatch(d -> d.equals(diaSemana));

                    if (ehFolga) {
                        continue; // pular a geração de escala nesse dia
                    }


                    Turnos turno = turnosDisponiveis.get(i % turnosDisponiveis.size());

                    Date dataEscalaDate = Date.from(dataEscala.atStartOfDay(ZoneId.systemDefault()).toInstant());

                    boolean escalaExiste = escalaRepository.existsByColaboradorIdAndDataEscala((long) colaborador.getId(), dataEscalaDate);

                    if (!escalaExiste) {
                        Escalas escala = new Escalas();
                        escala.setColaborador(colaborador);
                        escala.setTurnos(turno);
                        escala.setDataEscala(dataEscalaDate);
                        escala.setSetores(setor);
                        escala.setStatusEscala(Escalas.StatusEscala.CRIADO);

                        List<Escalas> escalasDaSemana = escalaRepository.findByColaboradorAndSemana(colaborador.getId(), hoje);
                        List<Escalas> escalasParaValidar = new ArrayList<>(escalasDaSemana);
                        escalasParaValidar.add(escala);

                        if (regrasCLTService.podeEscalar(contrato, cargo, escalasParaValidar)) {
                            escalasGeradas.add(escalaRepository.save(escala));
                        }
                    }

                }
            }
        }

        return escalasGeradas;
    }
}
