package br.com.sistemacadastro.sistemacadastro.modules.operador;


import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.Colaborador;
import br.com.sistemacadastro.sistemacadastro.modules.admin.setor.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.cargo.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.ColaboradorRepository;
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
@RequestMapping("/operador")
public class OperadorController {

    @Autowired
    private ColaboradorRepository collaboratorsRepository;

    @Autowired
    private CargoRepository cargosRepository;

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private SolicitacaoRepository solicitacoesRepository;




    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, HttpSession session) {
        long total = collaboratorsRepository.count();
        model.addAttribute("totalColaboradores", total);

        long totalSetor = setoresRepository.count();
        model.addAttribute("totalSetores", totalSetor);

        long totalCargo = cargosRepository.count();
        model.addAttribute("totalCargos", totalCargo);

        long totalSolicitacao = solicitacoesRepository.count();
        model.addAttribute("totalSolicitacoes", totalSolicitacao);

        // Pegar o nome do operador da sessão (igual já está no /minhaconta)
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Colaborador colaborador = collaboratorsRepository.findCollaboratorById(colaboradorId);
            if (colaborador != null) {
                String nomeCompleto = colaborador.getNome();
                model.addAttribute("nome", nomeCompleto);
                model.addAttribute("iniciais", getIniciais(nomeCompleto));
            }
        } else {
            // Se não tiver colaborador logado, pode colocar padrão
            model.addAttribute("nome", "Operador");
            model.addAttribute("iniciais", "O");
        }

        return "colaboradorpages/dashboard";
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

    @GetMapping("/minhaconta")
    public String mostrarMinhaConta(HttpSession session, Model model) {
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Colaborador colaborador = collaboratorsRepository.findCollaboratorById(colaboradorId);
            if (colaborador != null) {
                model.addAttribute("colaborador", colaborador);


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
            Colaborador colaborador = collaboratorsRepository.findCollaboratorById(colaboradorId);
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
        Colaborador colaborador = collaboratorsRepository.findCollaboratorByEmail(passwordChangeDto.getEmail());

        if (colaborador != null && colaborador.getSenha().equals(passwordChangeDto.getSenha())) {
            // Atualiza a senha
            colaborador.setSenha(passwordChangeDto.getNovaSenha());
            collaboratorsRepository.save(colaborador);
            model.addAttribute("message", "Senha alterada com sucesso!");
            return "redirect:/operador/minhaconta";
        } else {
            model.addAttribute("error", "A senha antiga está incorreta.");
            model.addAttribute("passwordChangeDto", passwordChangeDto);
        }

        return "colaboradorpages/alterarsenha";
    }
}



