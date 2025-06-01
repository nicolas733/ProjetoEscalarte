package br.com.sistemacadastro.sistemacadastro.controller;

import br.com.sistemacadastro.sistemacadastro.dto.EquipeDTO;
import br.com.sistemacadastro.sistemacadastro.dto.PasswordChangeDTO;
import br.com.sistemacadastro.sistemacadastro.model.*;
import br.com.sistemacadastro.sistemacadastro.model.Escalas.StatusEscala;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.EscalaRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.repository.TurnosRepository;
import br.com.sistemacadastro.sistemacadastro.service.EscalaService;
import br.com.sistemacadastro.sistemacadastro.service.GerenteService;
import br.com.sistemacadastro.sistemacadastro.util.UserSessionUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/gerente")
public class GerenteController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private GerenteService gerenteService;

    @Autowired
    private TurnosRepository turnosRepository;

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private EscalaRepository escalaRepository;

    @Autowired
    private EscalaService escalaService;

    private boolean verifyIsUserCredentialsCorrect(HttpSession session) {
        Long colaboradorId = UserSessionUtils.getIdUsuario(session);
        if (colaboradorId != null) {
            Colaborador colaborador = colaboradorRepository.findById(colaboradorId);
            if (colaborador != null && colaborador.getTipoUsuario() == Colaborador.TipoUsuario.GERENTE) {
                return true;
            }
        }

        return false;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        int countMembrosDaEquipe = 0;
        int countSolicitacoesPendentes = 0;
        int countTotalCargos = 0; // Pode implementar se quiser no futuro
        int countEventosProximos = 0; // Pode implementar se quiser no futuro

        // Recupera o ID do colaborador da sessão
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Colaborador colaborador = gerenteService.buscarColaboradorPorId(colaboradorId);
            if (colaborador != null) {
                // Nome e iniciais
                String nomeCompleto = colaborador.getNome();
                model.addAttribute("nome", nomeCompleto);
                model.addAttribute("iniciais", gerenteService.obterIniciais(nomeCompleto));

                // Conversão para Integer (id do gerente)
                Integer gerenteId = colaboradorId.intValue();

                // Contagem de membros da equipe
                List<Colaborador> equipe = gerenteService.listarColaboradoresPorSetorGerente(gerenteId);
                countMembrosDaEquipe = equipe.size();

                // Contagem de solicitações pendentes
                countSolicitacoesPendentes = gerenteService.contarSolicitacoesPendentesPorSetorDoGerente(gerenteId);

                // Se quiser, aqui também poderia buscar cargos, eventos etc.
            }
        }

        // Adiciona os valores ao modelo para uso no HTML
        model.addAttribute("countMembrosDaEquipe", countMembrosDaEquipe);
        model.addAttribute("countSolicitacoesPendentes", countSolicitacoesPendentes);
        model.addAttribute("countTotalCargos", countTotalCargos);
        model.addAttribute("countEventosProximos", countEventosProximos);

        return "gerentepages/dashboard";
    }

    @GetMapping("/escala")
    public String visualizarEscala(Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        // Pegar o setor do gerente
        Integer setorId = setoresRepository.findByGerenteId(UserSessionUtils.getIdUsuario(session))
                .stream().findFirst().map(Setores::getId).orElse(null);

        if (setorId == null) {
            model.addAttribute("ColaboradorDoSetor", Collections.emptyList());
            model.addAttribute("temEscala", false);
            model.addAttribute("setorSelecionado", null);
            return "gerentepages/escala";
        }

        model.addAttribute("setorSelecionado", setorId);
        model.addAttribute("turnos", turnosRepository.findAll());
        model.addAttribute("setorId", setorId);

        LocalDate hoje = LocalDate.now();
        LocalDate segunda = hoje.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate domingo = segunda.plusDays(6);

        List<LocalDate> diasSemana = new ArrayList<>();
        for (int i = 0; i <= 6; i++)
            diasSemana.add(segunda.plusDays(i));
        model.addAttribute("diasSemana", diasSemana);

        Date dataInicio = Date.valueOf(segunda);
        Date dataFim = Date.valueOf(domingo);

        List<Escalas> escalas = escalaRepository
                .findBySetoresIdAndDataEscalaBetweenOrderByDataEscala(setorId, dataInicio, dataFim)
                .stream()
                .filter(e -> e.getStatusEscala() == StatusEscala.EM_ANALISE || e.getStatusEscala() == StatusEscala.PUBLICADO)
                .toList();

        boolean temEscala = !escalas.isEmpty();
        model.addAttribute("temEscala", temEscala);

        // Usar o método do repositório que você já tem para buscar colaboradores ativos do setor
        List<Colaborador> colaboradoresDoSetor = colaboradorRepository.findBySetorAndContratoAtivo(setorId);
        model.addAttribute("ColaboradorDoSetor", colaboradoresDoSetor);

        Map<Colaborador, Map<LocalDate, List<Escalas>>> mapaEscalasPorData = new TreeMap<>(Comparator.comparing(Colaborador::getNome));

        Map<Colaborador, Map<LocalDate, List<Escalas>>> mapaTemp = escalas.stream()
                .collect(Collectors.groupingBy(
                        Escalas::getColaborador,
                        Collectors.groupingBy(e -> {
                            Timestamp ts = (Timestamp) e.getDataEscala(); // conversão segura
                            return ts.toLocalDateTime().toLocalDate();    // extrai o LocalDate
                        })
                ));

        mapaEscalasPorData.putAll(mapaTemp);

        model.addAttribute("mapaEscalasPorData", mapaEscalasPorData);

        return "gerentepages/escala";
    }

    @PostMapping("/modificar")
    public String alterarTurnoEscala(
            @RequestParam Integer colaboradorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") java.util.Date dataEscala,
            @RequestParam(required = false) String turnoId,
            RedirectAttributes redirectAttributes) {

        Turnos turno = null;

        // Trata caso de folga (turnoId null, vazio ou "folga")
        if (turnoId != null && !turnoId.trim().isEmpty() && !turnoId.equalsIgnoreCase("folga")) {
            try {
                int id = Integer.parseInt(turnoId);
                if (id > 0) {
                    turno = new Turnos();
                    turno.setId(id);
                }
            } catch (NumberFormatException e) {
                System.out.println("ID de turno inválido: " + turnoId);
            }
        } else {
            System.out.println("Folga selecionada.");
        }

        boolean alterado = escalaService.alterarTurnoEscala(colaboradorId, dataEscala, turno);

        if (alterado) {
            redirectAttributes.addFlashAttribute("msgSucesso", "Turno alterado com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("msgErro", "Não foi possível alterar o turno.");
        }

        return "redirect:/gerente/escala";
    }



    @GetMapping("/equipe")
    public String equipe(Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        Integer gerenteId = null;

        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        if (colaboradorIdObj instanceof Integer) {
            gerenteId = (Integer) colaboradorIdObj;
        } else if (colaboradorIdObj instanceof Long) {
            gerenteId = ((Long) colaboradorIdObj).intValue();
        }

        List<Colaborador> colaboradores = gerenteService.listarColaboradoresPorSetorGerente(gerenteId);

        List<EquipeDTO> equipeDTOList = colaboradores.stream()
                .map(colaborador -> {
                    EquipeDTO dto = new EquipeDTO();
                    dto.setId(colaborador.getId());
                    dto.setNome(colaborador.getNome());
                    dto.setEmail(colaborador.getEmail());
                    dto.setTelefone(colaborador.getTelefone());

                    if (colaborador.getContrato() != null && colaborador.getContrato().getCargos() != null) {
                        dto.setNomeCargo(colaborador.getContrato().getCargos().getNomeCargo());

                        var cargo = colaborador.getContrato().getCargos();
                        var cargoPorSetor = gerenteService.buscarCargoPorSetor(cargo.getId());

                        if (cargoPorSetor != null && cargoPorSetor.getSetor() != null) {
                            dto.setNomeSetor(cargoPorSetor.getSetor().getNomesetor());
                        } else {
                            dto.setNomeSetor("Setor não atribuído");
                        }
                    } else {
                        dto.setNomeCargo("Cargo não atribuído");
                        dto.setNomeSetor("Setor não atribuído");
                    }
                    dto.setTipoUsuario(colaborador.getTipoUsuario().name());
                    return dto;
                }).toList();

        model.addAttribute("equipe", equipeDTOList);
        return "gerentepages/equipe";
    }

    @GetMapping("/minhaconta")
    public String mostrarMinhaConta(HttpSession session, Model model) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        Long colaboradorId = UserSessionUtils.getIdUsuario(session);
        Colaborador colaborador = gerenteService.buscarColaboradorPorId(colaboradorId);

        model.addAttribute("colaborador", colaborador);

        return "gerentepages/minhaconta";
    }

    @GetMapping("/alterarsenha")
    public String mostrarAlterarSenha(HttpSession session, Model model) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        Long colaboradorId = UserSessionUtils.getIdUsuario(session);

        Colaborador colaborador = gerenteService.buscarColaboradorPorId(colaboradorId);
        PasswordChangeDTO passwordChangeDto = new PasswordChangeDTO();
        passwordChangeDto.setEmail(colaborador.getEmail());
        model.addAttribute("passwordChangeDto", passwordChangeDto);

        return "gerentepages/alterarsenha";
    }

    @PostMapping("/alterarsenha")
    public String alterarSenha(@ModelAttribute PasswordChangeDTO passwordChangeDto, Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        boolean sucesso = gerenteService.alterarSenha(passwordChangeDto);

        if (sucesso) {
            model.addAttribute("message", "Senha alterada com sucesso!");

            return "redirect:/gerente/minhaconta";
        } else {
            model.addAttribute("error", "A senha antiga está incorreta.");
            model.addAttribute("passwordChangeDto", passwordChangeDto);

            return "gerentepages/alterarsenha";
        }
    }

    @PostMapping("/solicitacoes/aprovar/{id}")
    @ResponseBody
    public ResponseEntity<String> aprovarSolicitacao(@PathVariable Integer id) {
        gerenteService.aprovarSolicitacao(id);
        return ResponseEntity.ok("Aprovado");
    }

    @PostMapping("/solicitacoes/recusar/{id}")
    @ResponseBody
    public ResponseEntity<String> recusarSolicitacao(@PathVariable Integer id) {
        gerenteService.recusarSolicitacao(id);
        return ResponseEntity.ok("Reprovado");
    }

}