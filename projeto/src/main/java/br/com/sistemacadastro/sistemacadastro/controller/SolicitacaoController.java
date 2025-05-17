package br.com.sistemacadastro.sistemacadastro.controller;

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

import br.com.sistemacadastro.sistemacadastro.colaborador.Colaborador;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SolicitacaoRepository;
import br.com.sistemacadastro.sistemacadastro.solicitacao.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.solicitacao.SolicitacoesDto;


@Controller
@RequestMapping("/solicitacao")
public class SolicitacaoController {

    @Autowired
    private ColaboradorRepository repo;

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
            Colaborador colaborador = repo.findById(colaboradorId);

            if (colaborador != null) {
                // Cria a solicitação e associa o colaborador
                Solicitacoes solicitacoes = new Solicitacoes();
                solicitacoes.setDataSolicitacao(solicitacoesDto.getDataSolicitacao());
                solicitacoes.setDescricaoSolicitacao(solicitacoesDto.getDescricaoSolicitacao());
                solicitacoes.setColaborador(colaborador);

                // Salva a solicitação no banco de dados
                solicitacaoRepository.save(solicitacoes);

                return "redirect:/operador/escala";
            }
        }

        return "redirect:/login"; // Caso o colaborador não esteja na sessão ou não seja encontrado
    }
}



