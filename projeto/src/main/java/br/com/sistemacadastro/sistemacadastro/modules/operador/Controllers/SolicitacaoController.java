package br.com.sistemacadastro.sistemacadastro.modules.operador.Controllers;

import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.CollaboratorRepository;
import br.com.sistemacadastro.sistemacadastro.modules.operador.Entitys.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.modules.operador.DTOs.SolicitacoesDto;
import br.com.sistemacadastro.sistemacadastro.modules.operador.repositorys.SolicitacaoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


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
    public String enviarSolicitacao(@Valid @ModelAttribute SolicitacoesDto solicitacoesDto, BindingResult result) {
        if (result.hasErrors()) {
            return "colaboradorpages/solicitacoes";
        }
        Solicitacoes solicitacoes = new Solicitacoes();
        solicitacoes.setDataSolicitacao(solicitacoesDto.getDataSolicitacao());
        solicitacoes.setDescricaoSolicitacao(solicitacoesDto.getDescricaoSolicitacao());


        solicitacaoRepository.save(solicitacoes);

        return "redirect:/operador/escala";
    }


}

