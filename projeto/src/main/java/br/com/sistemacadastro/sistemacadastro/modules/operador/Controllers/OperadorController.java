package br.com.sistemacadastro.sistemacadastro.modules.operador.Controllers;


import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.CollaboratorRepository;
import br.com.sistemacadastro.sistemacadastro.modules.operador.Entitys.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.modules.operador.repositorys.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/operador")
public class OperadorController {

    @Autowired
    private CollaboratorRepository collaboratorsRepository;

    @Autowired
    private CargoRepository cargosRepository;

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private SolicitacaoRepository solicitacoesRepository;



    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model) {
        long total = collaboratorsRepository.count(); // Conta diretamente no banco
        model.addAttribute("totalColaboradores", total);
        long totalSetor = setoresRepository.count();
        model.addAttribute("totalSetores", totalSetor);
        long totalCargo = cargosRepository.count();
        model.addAttribute("totalCargos", totalCargo);
        long totalSolicitacao = solicitacoesRepository.count();
        model.addAttribute("totalSolicitacoes", totalSolicitacao);
        return "colaboradorpages/dashboard";
    }

    @GetMapping("/escala")
    public String mostrarEscala(Model model) {
        return "colaboradorpages/escala";
    }

    @GetMapping("/solicitacoes")
    public String mostrarSolicitacao(Model model) {
        List<Solicitacoes> solicitacoes = solicitacoesRepository.findAll();
        model.addAttribute("solicitacoes", solicitacoes);
        model.addAttribute("solicitacao", new Solicitacoes());
        return "colaboradorpages/solicitacoes";
    }
}

