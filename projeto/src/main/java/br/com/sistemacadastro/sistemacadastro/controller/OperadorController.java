package br.com.sistemacadastro.sistemacadastro.controller;

import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.sistemacadastro.sistemacadastro.dto.PasswordChangeDTO;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Contrato;
import br.com.sistemacadastro.sistemacadastro.model.Escalas;
import br.com.sistemacadastro.sistemacadastro.model.Escalas.StatusEscala;
import br.com.sistemacadastro.sistemacadastro.model.Setores;
import br.com.sistemacadastro.sistemacadastro.model.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.repository.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.EscalaRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SolicitacoesRepository;
import br.com.sistemacadastro.sistemacadastro.util.UserSessionUtils;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/operador")
public class OperadorController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private SolicitacoesRepository solicitacoesRepository;

    @Autowired
    private EscalaRepository escalaRepository;

    private boolean verifyIsUserCredentialsCorrect(HttpSession session) {
        Long colaboradorId = UserSessionUtils.getIdUsuario(session);
        if (colaboradorId != null) {
            Colaborador colaborador = colaboradorRepository.findById(colaboradorId);
            if (colaborador != null && colaborador.getTipoUsuario() == Colaborador.TipoUsuario.OPERADOR) {
                return true;
            }
        }

        return false;
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        long total = colaboradorRepository.count();
        model.addAttribute("totalColaboradores", total);

        long totalSetor = setoresRepository.count();
        model.addAttribute("totalSetores", totalSetor);

        long totalCargo = cargoRepository.count();
        model.addAttribute("totalCargos", totalCargo);

        long totalSolicitacao = solicitacoesRepository.count();
        model.addAttribute("totalSolicitacoes", totalSolicitacao);

        Long colaboradorId = UserSessionUtils.getIdUsuario(session);

        if (colaboradorId != null) {
            Colaborador colaborador = colaboradorRepository.findById(colaboradorId);
            if (colaborador != null) {
                String nomeCompleto = colaborador.getNome();
                model.addAttribute("nome", nomeCompleto);
                model.addAttribute("iniciais", getIniciais(nomeCompleto));

                int solicitacoesPendentes = solicitacoesRepository
                        .countByColaboradorIdAndStatus(colaboradorId.intValue(), "Pendente");
                model.addAttribute("countSolicitacoesPendentes", solicitacoesPendentes);
            }
        } else {
            model.addAttribute("nome", "Operador");
            model.addAttribute("iniciais", "O");
            model.addAttribute("countSolicitacoesPendentes", 0);
        }

        return "colaboradorpages/dashboard";
    }

    private String getIniciais(String nome) {
        String[] partes = nome.trim().split(" ");
        if (partes.length >= 2) {
            return partes[0].substring(0, 1).toUpperCase() + partes[1].substring(0, 1).toUpperCase();
        } else if (partes.length == 1) {
            return partes[0].substring(0, 1).toUpperCase();
        }
        return "";
    }

    @GetMapping("/escala")
    public String visualizarEscala(Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }
        
        Long colaboradorId = UserSessionUtils.getIdUsuario(session);
        Setores setor = colaboradorRepository.findSetorByColaboradorId(colaboradorId);
        Integer setorId = (setor != null) ? setor.getId() : null;
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
                .filter(e -> (e.getStatusEscala() == StatusEscala.PUBLICADO)
                        && e.getColaboradorId() == (colaboradorId))
                .toList();

        Map<Colaborador, Map<LocalDate, List<Escalas>>> mapaEscalasPorData = new TreeMap<>(
                Comparator.comparing(Colaborador::getNome));

        escalas.stream()
                .collect(Collectors.groupingBy(
                        Escalas::getColaborador,
                        Collectors.groupingBy(e -> e.getDataEscala().toInstant()
                                .atZone(ZoneId.systemDefault()).toLocalDate())))
                .forEach(mapaEscalasPorData::put);

        Map<Colaborador, Set<LocalDate>> mapaFolgas = new HashMap<>();
        for (Colaborador colaborador : mapaEscalasPorData.keySet()) {
            Set<LocalDate> diasFolga = new HashSet<>();
            if (colaborador.getContrato() != null && colaborador.getContrato().getDiasFolga() != null) {
                for (Contrato.DiaFolga diaFolga : colaborador.getContrato().getDiasFolga()) {
                    diasFolga.add(segunda.with(DayOfWeek.valueOf(diaFolga.name())));
                }
            }
            mapaFolgas.put(colaborador, diasFolga);
        }

        model.addAttribute("mapaEscalasPorData", mapaEscalasPorData);
        model.addAttribute("mapaFolgas", mapaFolgas);

        return "colaboradorpages/escala";
    }

    @GetMapping("/solicitacoes")
    public String mostrarSolicitacao(Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        List<Solicitacoes> solicitacoes = solicitacoesRepository.findAll();
        model.addAttribute("solicitacoes", solicitacoes);
        model.addAttribute("solicitacao", new Solicitacoes());
        return "colaboradorpages/solicitacoes";
    }

    @GetMapping("/minhaconta")
    public String mostrarMinhaConta(HttpSession session, Model model) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        Long colaboradorId = UserSessionUtils.getIdUsuario(session);

        if (colaboradorId != null) {
            Colaborador colaborador = colaboradorRepository.findById(colaboradorId);
            if (colaborador != null) {
                model.addAttribute("colaborador", colaborador);

                return "colaboradorpages/minhaconta";
            }
        }

        return "redirect:/login";
    }

    @GetMapping("/alterarsenha")
    public String mostrarAlterarSenha(HttpSession session, Model model) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        Long colaboradorId = UserSessionUtils.getIdUsuario(session);

        Colaborador colaborador = colaboradorRepository.findById(colaboradorId);
        PasswordChangeDTO passwordChangeDto = new PasswordChangeDTO();
        passwordChangeDto.setEmail(colaborador.getEmail());
        model.addAttribute("passwordChangeDto", passwordChangeDto);
        return "colaboradorpages/alterarsenha";
    }

    @PostMapping("/alterarsenha")
    public String alterarSenha(@ModelAttribute PasswordChangeDTO passwordChangeDto, Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        Colaborador colaborador = colaboradorRepository.findCollaboratorByEmail(passwordChangeDto.getEmail());

        if (colaborador != null && colaborador.getSenha().equals(passwordChangeDto.getSenha())) {
            colaborador.setSenha(passwordChangeDto.getNovaSenha());
            colaboradorRepository.save(colaborador);
            model.addAttribute("message", "Senha alterada com sucesso!");
            return "redirect:/operador/minhaconta";
        } else {
            model.addAttribute("error", "A senha antiga está incorreta.");
            model.addAttribute("passwordChangeDto", passwordChangeDto);
        }

        return "colaboradorpages/alterarsenha";
    }

    @GetMapping("/solicita")
    public String visualizarMinhasSolicitacoes(Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        Long colaboradorId = UserSessionUtils.getIdUsuario(session);

        List<Solicitacoes> solicitacoes = solicitacoesRepository.findByColaboradorId(colaboradorId.intValue());
        model.addAttribute("solicitacoes", solicitacoes);
        return "colaboradorpages/solicita";
    }

}
