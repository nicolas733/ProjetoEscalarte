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
import br.com.sistemacadastro.sistemacadastro.repository.SolicitacaoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ColaboradorRepository repo;

    @Autowired
    private CargoRepository repository;

    @Autowired
    private SetoresRepository reposito;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private CargosPorSetorRepository cargosPorSetorRepository;

    @GetMapping({ "/main" })

    public String listarDados(HttpSession session, Model model) {
        Object user = session.getAttribute("colaboradorId");
        if (user == null) {
            return "redirect:/login";
        }
        List<Colaborador> colaboradores = repo.findAll();
        model.addAttribute("colaboradores", colaboradores);
        model.addAttribute("colaborador", new Colaborador());
        return "adminpages/usuarios";
    }

    @GetMapping("/dashboard")
    public <Collaborator> String mostrarDashboard(Model model, HttpSession session) {
        long total = repo.count();
        model.addAttribute("totalColaboradores", total);

        long totalSetor = reposito.count();
        model.addAttribute("totalSetores", totalSetor);

        long totalCargo = repository.count();
        model.addAttribute("totalCargos", totalCargo);

        long totalSolicitacao = solicitacaoRepository.count();
        model.addAttribute("totalSolicitacoes", totalSolicitacao);

        // Recuperar colaborador logado da sessão
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Collaborator colaborador = (Collaborator) repo.findCollaboratorById(colaboradorId);
            if (colaborador != null) {
                String nomeCompleto = ((Colaborador) colaborador).getNome();
                model.addAttribute("nome", nomeCompleto);
                model.addAttribute("iniciais", getIniciais(nomeCompleto));
            }
        } else {
            model.addAttribute("nome", "Admin");
            model.addAttribute("iniciais", "A");
        }

        return "adminpages/dashboard";
    }

    private String getIniciais(String nomeCompleto) {
        String[] partes = nomeCompleto.split(" ");
        if (partes.length == 1) {
            return partes[0].substring(0, 1).toUpperCase();
        }
        return (partes[0].substring(0, 1) + partes[partes.length - 1].substring(0, 1)).toUpperCase();
    }

    @GetMapping("/setorcargo")
    public String mostrarSetoresCargos(Model model) {
        List<Setores> setores = reposito.findAll();
        model.addAttribute("setores", setores);
        model.addAttribute("novoSetor", new Setores());
        List<CargosPorSetor> cargos = cargosPorSetorRepository.findAll();
        model.addAttribute("cargosPorSetor", cargos);
        model.addAttribute("novoCargo", new Cargos());

        return "adminpages/setores";
    }

    @GetMapping("/escala")
    public String mostrarEscala(Model model) {
        return "adminpages/escala";
    }

    @GetMapping("/solici")
    public String mostrarSolicitacao(Model model) {
        List<Solicitacoes> solicitacoes = solicitacaoRepository.findAll();
        model.addAttribute("solicitacoes", solicitacoes);
        model.addAttribute("solicitacao", new Solicitacoes());
        return "adminpages/Solici";
    }// teste

    @GetMapping("/minhaconta")
    public String mostrarMinhaConta(HttpSession session, Model model) {
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Colaborador colaborador = repo.findById(colaboradorId);
            if (colaborador != null) {
                model.addAttribute("colaborador", colaborador);

                return "adminpages/minhaconta";
            }
        }

        return "redirect:/login";
    }

    @GetMapping("/alterarsenha")
    public String mostrarAlterarSenha(HttpSession session, Model model) {
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Colaborador colaborador = repo.findById(colaboradorId);
            if (colaborador != null) {
                PasswordChangeDTO passwordChangeDto = new PasswordChangeDTO();
                passwordChangeDto.setEmail(colaborador.getEmail());
                model.addAttribute("passwordChangeDto", passwordChangeDto);
                return "adminpages/alterarsenha";
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/alterarsenha")
    public String alterarSenha(@ModelAttribute PasswordChangeDTO passwordChangeDto, Model model) {
        // Recupera o colaborador
        Colaborador colaborador = repo.findCollaboratorByEmail(passwordChangeDto.getEmail());

        if (colaborador != null && colaborador.getSenha().equals(passwordChangeDto.getSenha())) {
            // Atualiza a senha
            colaborador.setSenha(passwordChangeDto.getNovaSenha());
            repo.save(colaborador);
            model.addAttribute("message", "Senha alterada com sucesso!");
            return "redirect:/admin/minhaconta";
        } else {
            model.addAttribute("error", "A senha antiga está incorreta.");
            model.addAttribute("passwordChangeDto", passwordChangeDto);
        }

        return "adminpages/alterarsenha";
    }
}