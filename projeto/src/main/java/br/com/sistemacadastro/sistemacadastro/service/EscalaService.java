package br.com.sistemacadastro.sistemacadastro.service;

import java.time.LocalDate;
import java.time.ZoneId;
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

    /**
     * Método que gera escalas semanais por setor.
     *
     * @return lista de escalas geradas
     */
    public List<Escalas> gerarEscalasSemanais() {
        List<Escalas> escalasGeradas = new ArrayList<>();

        // Buscar todos os setores ativos
        List<Setores> setores = setoresRepository.findAll();

        LocalDate hoje = LocalDate.now();

        for (Setores setor : setores) {
            // Buscar colaboradores ativos no setor (assumindo método no repo que filtra por
            // setor e contrato ativo)
            List<Colaborador> colaboradoresAtivos = colaboradorRepository.findBySetorAndContratoAtivo(setor.getId());

            for (Colaborador colaborador : colaboradoresAtivos) {
                Contrato contrato = colaborador.getContrato(); // método que retorna contrato ativo do colaborador
                Cargos cargo = colaborador.getContrato().getCargos();

                // Buscar os turnos disponíveis para o setor ou para o colaborador (supondo que
                // os turnos são relacionados com colaborador)
                List<Turnos> turnosDisponiveis = colaborador.getTurnos();
                if (turnosDisponiveis.isEmpty()) {
                    continue; // Não tem turno definido para esse colaborador, não gera escala
                }

                // Criar escalas para os próximos 7 dias
                for (int i = 0; i < 7; i++) {
                    LocalDate dataEscala = hoje.plusDays(i);

                    // Escolher um turno para esse dia (exemplo: rotacionar turnos)
                    Turnos turnoParaDia = turnosDisponiveis.get(i % turnosDisponiveis.size());

                    // Criar a escala temporária para validar regras
                    Escalas escalaTemp = new Escalas();
                    escalaTemp.setColaborador(colaborador);
                    escalaTemp.setTurnos(turnoParaDia);
                    escalaTemp.setDataEscala(Date.from(dataEscala.atStartOfDay(ZoneId.systemDefault()).toInstant()));
                    escalaTemp.setSetores(setor);

                    // Para validar as regras, passar uma lista contendo a escala criada e outras
                    // escalas do colaborador
                    // Supondo que você pode buscar escalas já criadas para o colaborador nesta
                    // semana para validar interjornada
                    List<Escalas> escalasDoColaboradorNaSemana = escalaRepository
                            .findByColaboradorAndSemana(colaborador.getId(), hoje);

                    // Montar lista com as escalas já existentes + a nova que quer inserir
                    List<Escalas> escalasParaValidar = new ArrayList<>(escalasDoColaboradorNaSemana);
                    escalasParaValidar.add(escalaTemp);

                    // Verificar regras CLT
                    boolean pode = regrasCLTService.podeEscalar(contrato, cargo, escalasParaValidar);

                    if (pode) {
                        // Salvar escala no banco e adicionar na lista de retorno
                        Escalas escalaSalva = escalaRepository.save(escalaTemp);
                        escalasGeradas.add(escalaSalva);
                    } else {
                        // Não gera escala para esse dia para esse colaborador
                        // Pode adicionar lógica para tentar outro turno, etc (a critério)
                    }
                }
            }
        }

        escalaRepository.saveAll(escalasGeradas);
        return escalasGeradas;
    }
}
