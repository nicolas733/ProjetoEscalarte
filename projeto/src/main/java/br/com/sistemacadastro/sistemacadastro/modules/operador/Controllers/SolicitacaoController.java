package br.com.sistemacadastro.sistemacadastro.modules.operador.Controllers;

import br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs.CollaboratorDto;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Contrato;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Endereco;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.CollaboratorRepository;
import br.com.sistemacadastro.sistemacadastro.modules.operador.Entitys.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.modules.operador.DTOs.SolicitacoesDto;
import br.com.sistemacadastro.sistemacadastro.modules.operador.repositorys.SolicitacaoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;


@Controller
@RequestMapping("/solicitacao")
public class SolicitacaoController {

    @Autowired
    private CollaboratorRepository repo;

    @Autowired
    private SolicitacaoRepository solicitacaoRepository;



    @GetMapping("/alteracao")
    public String mostrarFormulario(Model model) {
        SolicitacoesDto solicitacoesDto = new SolicitacoesDto();
        model.addAttribute("solicitacaoDto", solicitacoesDto);
        return "colaboradorpages/solicitacoes"; // HTML com o form
    }


    @PostMapping("/alteracao")
    public String enviarSolicitacao(@Valid @ModelAttribute SolicitacoesDto solicitacoesDto, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("solicitacaoDto", solicitacoesDto);
            return "colaboradorpages/solicitacoes";
        }

        Object colaboradorIdObj = session.getAttribute("colaboradorId");

        if (colaboradorIdObj != null) {
            Integer colaboradorId = (Integer) colaboradorIdObj;

            // Busca o colaborador no banco de dados
            Collaborator colaborador = repo.findById(colaboradorId);

            if (colaborador != null) {
                // Cria a solicitação e associa o colaborador
                Solicitacoes solicitacoes = new Solicitacoes();
                solicitacoes.setDataSolicitacao(solicitacoesDto.getDataSolicitacao());
                solicitacoes.setDescricaoSolicitacao(solicitacoesDto.getDescricaoSolicitacao());
                solicitacoes.setCollaborator(colaborador);

                // Salva a solicitação no banco de dados
                solicitacaoRepository.save(solicitacoes);

                return "redirect:/operador/escala";
            }
        }

        return "redirect:/login"; // Caso o colaborador não esteja na sessão ou não seja encontrado
    }
}



