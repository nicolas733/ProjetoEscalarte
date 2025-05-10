package br.com.sistemacadastro.sistemacadastro.modules.gerente;

import br.com.sistemacadastro.sistemacadastro.modules.admin.setor.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.cargo.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.CollaboratorRepository;
import br.com.sistemacadastro.sistemacadastro.modules.operador.solicitacao.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.modules.operador.solicitacao.SolicitacaoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/gerente")
public class GerenteController {

    @Autowired
    private CollaboratorRepository colaboradoresRepository;

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
            var colaborador = colaboradoresRepository.findCollaboratorById(colaboradorId);
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
}
