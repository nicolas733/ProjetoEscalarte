package br.com.sistemacadastro.sistemacadastro.modules;

import br.com.sistemacadastro.sistemacadastro.modules.Setores.repository.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.modules.cargos.repository.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.collaborator.repository.CollaboratorRepository;
import br.com.sistemacadastro.sistemacadastro.modules.solicitacoes.model.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.modules.solicitacoes.repository.SolicitacaoRepository;
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


    @GetMapping("")
    public String dashboard(Model model) {
        int countMembrosDaEquipe = 0;
        model.addAttribute("countMembrosDaEquipe", countMembrosDaEquipe);
        int countSolicitacoesPendentes = 0;
        model.addAttribute("countSolicitacoesPendentes", countSolicitacoesPendentes);
        int countTotalCargos = 0;
        model.addAttribute("countTotalCargos", countTotalCargos);
        int countEventosProximos = 0;
        model.addAttribute("countEventosProximos", countEventosProximos);
        return "gerentepages/dashboard";
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
