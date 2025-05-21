package br.com.sistemacadastro.sistemacadastro.controller;

import br.com.sistemacadastro.sistemacadastro.dto.PasswordChangeDTO;
import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.model.CargosPorSetor;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Setores;
import br.com.sistemacadastro.sistemacadastro.model.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.repository.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.repository.CargosPorSetorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SolicitacoesRepository;
import br.com.sistemacadastro.sistemacadastro.util.SessionUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private SolicitacoesRepository solicitacoesRepository;

    @Autowired
    private CargosPorSetorRepository cargosPorSetorRepository;

    private String rotaPrivada(String rota, HttpSession session) {
        Long colaboradorId = SessionUtils.getIdUsuario(session);
        if (colaboradorId != null) {
            Colaborador colaborador = colaboradorRepository.findById(colaboradorId);
            if (colaborador != null && colaborador.getTipoUsuario() == Colaborador.TipoUsuario.ADMIN) {
                return rota;
            }
        }

        return "redirect:/login";
    }

    @GetMapping({ "/main" })

    public String listarDados(HttpSession session, Model model) {
        List<Colaborador> colaboradores = colaboradorRepository.findAll();
        model.addAttribute("colaboradores", colaboradores);
        model.addAttribute("colaborador", new Colaborador());
        return rotaPrivada("adminpages/usuarios", session);
    }

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, HttpSession session) {
        long total = colaboradorRepository.count();
        model.addAttribute("totalColaboradores", total);

        long totalSetor = setoresRepository.count();
        model.addAttribute("totalSetores", totalSetor);

        long totalCargo = cargoRepository.count();
        model.addAttribute("totalCargos", totalCargo);

        long totalSolicitacao = solicitacoesRepository.count();
        model.addAttribute("totalSolicitacoes", totalSolicitacao);

        // Recuperar colaborador logado da sessão
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Colaborador colaborador = colaboradorRepository.findById(colaboradorId);
            if (colaborador != null) {
                String nomeCompleto = ((Colaborador) colaborador).getNome();
                model.addAttribute("nome", nomeCompleto);
                model.addAttribute("iniciais", getIniciais(nomeCompleto));
            }
        } else {
            model.addAttribute("nome", "Admin");
            model.addAttribute("iniciais", "A");
        }

        return rotaPrivada("adminpages/dashboard", session);
    }

    private String getIniciais(String nomeCompleto) {
        String[] partes = nomeCompleto.split(" ");
        if (partes.length == 1) {
            return partes[0].substring(0, 1).toUpperCase();
        }
        return (partes[0].substring(0, 1) + partes[partes.length - 1].substring(0, 1)).toUpperCase();
    }

    @GetMapping("/setorcargo")
    public String mostrarSetoresCargos(Model model, HttpSession session) {
        List<Setores> setores = setoresRepository.findAll();
        model.addAttribute("setores", setores);
        model.addAttribute("novoSetor", new Setores());
        List<CargosPorSetor> cargos = cargosPorSetorRepository.findAll();
        model.addAttribute("cargosPorSetor", cargos);
        model.addAttribute("novoCargo", new Cargos());

        return rotaPrivada("adminpages/setores", session);
    }

    @GetMapping("/escala")
    public String mostrarEscala(Model model, HttpSession session) {
        return rotaPrivada("adminpages/escala", session);
    }

    @GetMapping("/solici")
    public String mostrarSolicitacao(Model model, HttpSession session) {
        List<Solicitacoes> solicitacoes = solicitacoesRepository.findAll();
        model.addAttribute("solicitacoes", solicitacoes);
        model.addAttribute("solicitacao", new Solicitacoes());

        // Mapa de cargos
        Map<Integer, String> cargosMap = new HashMap<>();
        // Mapa de setores
        Map<Integer, String> setoresMap = new HashMap<>();

        List<Colaborador> colaboradores = colaboradorRepository.findAll();
        for (Colaborador c : colaboradores) {
            if (c.getCargoPorSetor() != null) {
                if (c.getCargoPorSetor().getCargo() != null) {
                    cargosMap.put(c.getId(), c.getCargoPorSetor().getCargo().getNomeCargo());
                }
                if (c.getCargoPorSetor().getSetor() != null) {
                    setoresMap.put(c.getId(), c.getCargoPorSetor().getSetor().getNomesetor());
                }
            }
        }

        model.addAttribute("cargos", cargosMap);
        model.addAttribute("setores", setoresMap);

        return rotaPrivada("adminpages/Solici", session);
    }


    @GetMapping("/minhaconta")
    public String mostrarMinhaConta(HttpSession session, Model model) {
        Long colaboradorId = SessionUtils.getIdUsuario(session);
        Colaborador colaborador = colaboradorRepository.findById(colaboradorId);
        model.addAttribute("colaborador", colaborador);

        return rotaPrivada("adminpages/minhaconta", session);
    }

    @GetMapping("/alterarsenha")
    public String mostrarAlterarSenha(HttpSession session, Model model) {
        Long colaboradorId = SessionUtils.getIdUsuario(session);
        Colaborador colaborador = colaboradorRepository.findById(colaboradorId);
        PasswordChangeDTO passwordChangeDto = new PasswordChangeDTO();
        passwordChangeDto.setEmail(colaborador.getEmail());
        model.addAttribute("passwordChangeDto", passwordChangeDto);

        return rotaPrivada("adminpages/alterarsenha", session);
    }

    @PostMapping("/alterarsenha")
    public String alterarSenha(@ModelAttribute PasswordChangeDTO passwordChangeDto, Model model, HttpSession session) {
        // Recupera o colaborador
        Colaborador colaborador = colaboradorRepository.findCollaboratorByEmail(passwordChangeDto.getEmail());

        if (colaborador != null && colaborador.getSenha().equals(passwordChangeDto.getSenha())) {
            // Atualiza a senha
            colaborador.setSenha(passwordChangeDto.getNovaSenha());
            colaboradorRepository.save(colaborador);
            model.addAttribute("message", "Senha alterada com sucesso!");
            return "redirect:/admin/minhaconta";
        } else {
            model.addAttribute("error", "A senha antiga está incorreta.");
            model.addAttribute("passwordChangeDto", passwordChangeDto);
        }
        
        return rotaPrivada("adminpages/alterarsenha", session);
    }

    @PostMapping("/solicitacoes/aprovar/{id}")
    @ResponseBody
    public String aprovarSolicitacao(@PathVariable int id) {
        Solicitacoes solicitacao = solicitacoesRepository.findById(id).orElse(null);
        if (solicitacao != null && solicitacao.getStatus().equals("Pendente")) {
            solicitacao.setStatus("Aprovado");
            solicitacoesRepository.save(solicitacao);
            return "OK";
        }
        return "Erro";
    }

    @PostMapping("/solicitacoes/recusar/{id}")
    @ResponseBody
    public String recusarSolicitacao(@PathVariable int id) {
        Solicitacoes solicitacao = solicitacoesRepository.findById(id).orElse(null);
        if (solicitacao != null && solicitacao.getStatus().equals("Pendente")) {
            solicitacao.setStatus("Reprovado");
            solicitacoesRepository.save(solicitacao);
            return "OK";
        }
        return "Erro";
    }

}