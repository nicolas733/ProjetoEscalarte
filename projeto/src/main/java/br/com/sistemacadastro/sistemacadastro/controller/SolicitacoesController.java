package br.com.sistemacadastro.sistemacadastro.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import br.com.sistemacadastro.sistemacadastro.dto.SolicitacoesDTO;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SolicitacoesRepository;

import java.util.Objects;


@Controller
@RequestMapping("/solicitacoes")
public class SolicitacoesController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private SolicitacoesRepository solicitacoesRepository;



    @GetMapping("/alteracao")
    public String mostrarFormulario(Model model) {
        SolicitacoesDTO solicitacoesDto = new SolicitacoesDTO();
        model.addAttribute("solicitacaoDTO", solicitacoesDto);
        return "colaboradorpages/solicitacoes"; // HTML com o form
    }


    @PostMapping("/alteracao")
    public String enviarSolicitacao(@Valid @ModelAttribute("solicitacoesDto") SolicitacoesDTO solicitacoesDto, BindingResult result, Model model, HttpSession session) {
        if (result.hasErrors()) {
            model.addAttribute("solicitacaoDTO", solicitacoesDto);
            return "colaboradorpages/solicitacoes";
        }

        Object colaboradorIdObj = session.getAttribute("colaboradorId");

        if (colaboradorIdObj != null) {
            Integer colaboradorId = (Integer) colaboradorIdObj;

            // Busca o colaborador no banco de dados
            Colaborador colaborador = colaboradorRepository.findById(colaboradorId)
                    .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

            if (colaborador != null) {
                // Cria a solicitação e associa o colaborador
                Solicitacoes solicitacoes = new Solicitacoes();
                solicitacoes.setDataSolicitacao(solicitacoesDto.getDataSolicitacao());
                solicitacoes.setDescricaoSolicitacao(solicitacoesDto.getDescricaoSolicitacao());
                solicitacoes.setColaborador(colaborador);

                // Salva a solicitação no banco de dados
                solicitacoesRepository.save(solicitacoes);

                return "redirect:/solicitacoes/alteracao?sucesso=true";
            }
        }

        return "redirect:/login";
    }

    @PostMapping("/excluir/{id}")
    public String excluirSolicitacao(@PathVariable Integer id, HttpSession session) {

        Object colaboradorIdObj = session.getAttribute("colaboradorId");

        if (colaboradorIdObj != null) {
            Integer colaboradorId = (Integer) colaboradorIdObj;

            Solicitacoes solicitacao = solicitacoesRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));


            if (Objects.equals(solicitacao.getColaborador().getId(), colaboradorId)
                    && "Pendente".equalsIgnoreCase(solicitacao.getStatus())) {
                solicitacoesRepository.delete(solicitacao);
            }

        }

        return "redirect:/operador/solicita";
    }

}



