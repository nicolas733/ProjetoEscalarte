package br.com.sistemacadastro.sistemacadastro.modules.gerente;

import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.admin.setor.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.cargo.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.CollaboratorRepository;
import br.com.sistemacadastro.sistemacadastro.modules.operador.meuUsuario.PasswordChangeDTO;
import br.com.sistemacadastro.sistemacadastro.modules.operador.solicitacao.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.modules.operador.solicitacao.SolicitacaoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/gerente")
public class GerenteController {

    @Autowired
    private CollaboratorRepository collaboratorsRepository;

    @Autowired
    private CargoRepository cargosRepository;

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private SolicitacaoRepository solicitacoesRepository;


    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        int countMembrosDaEquipe = 0;
        int countSolicitacoesPendentes = 0;
        int countTotalCargos = 0;
        int countEventosProximos = 0;

        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            var colaborador = collaboratorsRepository.findById(colaboradorId);
            if (colaborador != null) {
                String nomeCompleto = colaborador.getNome();
                model.addAttribute("nome", nomeCompleto);
                String iniciais = getIniciais(nomeCompleto);
                model.addAttribute("iniciais", iniciais);
            }
        }

        model.addAttribute("countMembrosDaEquipe", countMembrosDaEquipe);
        model.addAttribute("countSolicitacoesPendentes", countSolicitacoesPendentes);
        model.addAttribute("countTotalCargos", countTotalCargos);
        model.addAttribute("countEventosProximos", countEventosProximos);

        return "gerentepages/dashboard";
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

    @GetMapping("/solicitacoes")
    public String solicitacoes (Model model) {
        List<Solicitacoes> solicitacoes = solicitacoesRepository.findAll();
        model.addAttribute("solicitacoes", solicitacoes);
        model.addAttribute("solicitacao", new Solicitacoes());
        return "gerentepages/solicitacoes";
    }

    @GetMapping("/escala")
    public String escala (Model model) {
        return "gerentepages/escala";
    }

    @GetMapping("/equipe")
    public String equipe (Model model) {
        return "gerentepages/equipe";
    }

    @GetMapping("/minhaconta")
    public String mostrarMinhaConta(HttpSession session, Model model) {
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Collaborator colaborador = collaboratorsRepository.findById(colaboradorId);
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
            Collaborator colaborador = collaboratorsRepository.findById(colaboradorId);
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
        Collaborator colaborador = collaboratorsRepository.findCollaboratorByEmail(passwordChangeDto.getEmail());

        if (colaborador != null && colaborador.getSenha().equals(passwordChangeDto.getSenha())) {
            // Atualiza a senha
            colaborador.setSenha(passwordChangeDto.getNovaSenha());
            collaboratorsRepository.save(colaborador);
            model.addAttribute("message", "Senha alterada com sucesso!");
            return "redirect:/gerente/minhaconta";
        } else {
            model.addAttribute("error", "A senha antiga está incorreta.");
            model.addAttribute("passwordChangeDto", passwordChangeDto);
        }

        return "colaboradorpages/alterarsenha";
    }
}
