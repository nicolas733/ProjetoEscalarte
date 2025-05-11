package br.com.sistemacadastro.sistemacadastro.modules.admin;


import br.com.sistemacadastro.sistemacadastro.modules.admin.cargosPorSetor.CargosPorSetor;
import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.admin.setor.Setores;
import br.com.sistemacadastro.sistemacadastro.modules.admin.cargo.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.cargosPorSetor.CargosPorSetorRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.setor.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.cargo.Cargos;
import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.CollaboratorRepository;
import br.com.sistemacadastro.sistemacadastro.modules.operador.meuUsuario.PasswordChangeDTO;
import br.com.sistemacadastro.sistemacadastro.modules.operador.solicitacao.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.modules.operador.solicitacao.SolicitacaoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class MainController {

    @Autowired
    private CollaboratorRepository repo;

    @Autowired
    private CargoRepository repository;

    @Autowired
    private SetoresRepository reposito;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;

    @Autowired
    private CargosPorSetorRepository cargosPorSetorRepository;



    @GetMapping({"/main"})
    public String listarDados(Model model) {
        List<Collaborator> collaborators = repo.findAll();
        model.addAttribute("collaborators", collaborators);
        model.addAttribute("collaborator", new Collaborator());
        return "adminpages/usuarios";
    }




    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        long total = repo.count(); // Conta diretamente no banco
        model.addAttribute("totalColaboradores", total);
        long totalSetor = reposito.count();
        model.addAttribute("totalSetores", totalSetor);
        long totalCargo = repository.count();
        model.addAttribute("totalCargos", totalCargo);
        long totalSolicitacao = solicitacaoRepository.count();
        model.addAttribute("totalSolicitacoes", totalSolicitacao);
        return "adminpages/dashboard";
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
    }//teste

    @GetMapping("/minhaconta")
    public String mostrarMinhaConta(HttpSession session, Model model) {
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Collaborator colaborador = repo.findById(colaboradorId);
            if (colaborador != null) {
                model.addAttribute("collaborator", colaborador);


                return "colaboradorpages/minhaconta";
            }
        }

        return "redirect:/login";
    }

    @GetMapping("/alterarsenha")
    public String mostrarAlterarSenha(HttpSession session, Model model) {
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Collaborator colaborador = repo.findById(colaboradorId);
            if (colaborador != null) {
                PasswordChangeDTO passwordChangeDto = new PasswordChangeDTO();
                passwordChangeDto.setEmail(colaborador.getEmail());
                model.addAttribute("passwordChangeDto", passwordChangeDto);
                return "colaboradorpages/alterarsenha";
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/alterarsenha")
    public String alterarSenha(@ModelAttribute PasswordChangeDTO passwordChangeDto, Model model) {
        // Recupera o colaborador
        Collaborator colaborador = repo.findCollaboratorByEmail(passwordChangeDto.getEmail());

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

        return "colaboradorpages/alterarsenha";
    }
}

