package br.com.sistemacadastro.sistemacadastro.controller;

import br.com.sistemacadastro.sistemacadastro.dto.EquipeDTO;
import br.com.sistemacadastro.sistemacadastro.dto.PasswordChangeDTO;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.service.GerenteService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/gerente")
public class GerenteController {

    @Autowired
    private GerenteService gerenteService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        int countMembrosDaEquipe = 0;
        int countSolicitacoesPendentes = 0;
        int countTotalCargos = 0;
        int countEventosProximos = 0;

        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Colaborador colaborador = gerenteService.buscarColaboradorPorId(colaboradorId);
            if (colaborador != null) {
                String nomeCompleto = colaborador.getNome();
                model.addAttribute("nome", nomeCompleto);
                model.addAttribute("iniciais", gerenteService.obterIniciais(nomeCompleto));
            }
        }

        model.addAttribute("countMembrosDaEquipe", countMembrosDaEquipe);
        model.addAttribute("countSolicitacoesPendentes", countSolicitacoesPendentes);
        model.addAttribute("countTotalCargos", countTotalCargos);
        model.addAttribute("countEventosProximos", countEventosProximos);

        return "gerentepages/dashboard";
    }

    @GetMapping("/solicitacoes")
    public String solicitacoes(Model model) {
        List<Solicitacoes> solicitacoes = gerenteService.listarSolicitacoes();
        model.addAttribute("solicitacoes", solicitacoes);
        model.addAttribute("solicitacao", new Solicitacoes());
        return "gerentepages/solicitacoes";
    }

    @GetMapping("/escala")
    public String escala(Model model) {
        return "gerentepages/escala";
    }

    @GetMapping("/equipe")
    public String equipe(Model model, HttpSession session) {
        Integer gerenteId = null;

        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        if (colaboradorIdObj instanceof Integer) {
            gerenteId = (Integer) colaboradorIdObj;
        } else if (colaboradorIdObj instanceof Long) {
            gerenteId = ((Long) colaboradorIdObj).intValue();
        }

        List<Colaborador> colaboradores = gerenteService.listarColaboradoresPorSetorGerente(gerenteId);

        List<EquipeDTO> equipeDTOList = colaboradores.stream()
                .map(colaborador -> {
                    EquipeDTO dto = new EquipeDTO();
                    dto.setId(colaborador.getId());
                    dto.setNome(colaborador.getNome());
                    dto.setEmail(colaborador.getEmail());
                    dto.setCpf(colaborador.getCpf());

                    if (colaborador.getContrato() != null && colaborador.getContrato().getCargos() != null) {
                        dto.setNomeCargo(colaborador.getContrato().getCargos().getNomeCargo());

                        var cargo = colaborador.getContrato().getCargos();
                        var cargoPorSetor = gerenteService.buscarCargoPorSetor(cargo.getId());

                        if (cargoPorSetor != null && cargoPorSetor.getSetor() != null) {
                            dto.setNomeSetor(cargoPorSetor.getSetor().getNomesetor());
                        } else {
                            dto.setNomeSetor("Setor não atribuído");
                        }
                    } else {
                        dto.setNomeCargo("Cargo não atribuído");
                        dto.setNomeSetor("Setor não atribuído");
                    }
                    dto.setTipoUsuario(colaborador.getTipoUsuario().name());
                    return dto;
                }).toList();

        model.addAttribute("equipe", equipeDTOList);
        return "gerentepages/equipe";
    }


    @GetMapping("/minhaconta")
    public String mostrarMinhaConta(HttpSession session, Model model) {
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Colaborador colaborador = gerenteService.buscarColaboradorPorId(colaboradorId);
            if (colaborador != null) {
                model.addAttribute("colaborador", colaborador);
                return "gerentepages/minhaconta";
            }
        }
        return "redirect:/login";
    }


    @GetMapping("/alterarsenha")
    public String mostrarAlterarSenha(HttpSession session, Model model) {
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Colaborador colaborador = gerenteService.buscarColaboradorPorId(colaboradorId);
            if (colaborador != null) {
                PasswordChangeDTO passwordChangeDto = new PasswordChangeDTO();
                passwordChangeDto.setEmail(colaborador.getEmail());
                model.addAttribute("passwordChangeDto", passwordChangeDto);
                return "gerentepages/alterarsenha";
            }
        }
        return "redirect:/login";
    }


    @PostMapping("/alterarsenha")
    public String alterarSenha(@ModelAttribute PasswordChangeDTO passwordChangeDto, Model model) {
        boolean sucesso = gerenteService.alterarSenha(passwordChangeDto);

        if (sucesso) {
            model.addAttribute("message", "Senha alterada com sucesso!");
            return "redirect:/gerente/minhaconta";
        } else {
            model.addAttribute("error", "A senha antiga está incorreta.");
            model.addAttribute("passwordChangeDto", passwordChangeDto);
            return "gerentepages/alterarsenha";
        }
    }
}