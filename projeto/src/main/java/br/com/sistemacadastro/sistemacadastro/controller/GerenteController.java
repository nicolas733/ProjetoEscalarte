package br.com.sistemacadastro.sistemacadastro.controller;

import br.com.sistemacadastro.sistemacadastro.dto.EquipeDTO;
import br.com.sistemacadastro.sistemacadastro.dto.PasswordChangeDTO;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.service.GerenteService;
import br.com.sistemacadastro.sistemacadastro.util.UserSessionUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/gerente")
public class GerenteController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private GerenteService gerenteService;

    private boolean verifyIsUserCredentialsCorrect(HttpSession session) {
        Long colaboradorId = UserSessionUtils.getIdUsuario(session);
        if (colaboradorId != null) {
            Colaborador colaborador = colaboradorRepository.findById(colaboradorId);
            if (colaborador != null && colaborador.getTipoUsuario() == Colaborador.TipoUsuario.GERENTE) {
                return true;
            }
        }

        return false;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        int countMembrosDaEquipe = 0;
        int countSolicitacoesPendentes = 0;
        int countTotalCargos = 0; // Pode implementar se quiser no futuro
        int countEventosProximos = 0; // Pode implementar se quiser no futuro

        // Recupera o ID do colaborador da sessão
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = colaboradorIdObj != null ? ((Number) colaboradorIdObj).longValue() : null;

        if (colaboradorId != null) {
            Colaborador colaborador = gerenteService.buscarColaboradorPorId(colaboradorId);
            if (colaborador != null) {
                // Nome e iniciais
                String nomeCompleto = colaborador.getNome();
                model.addAttribute("nome", nomeCompleto);
                model.addAttribute("iniciais", gerenteService.obterIniciais(nomeCompleto));

                // Conversão para Integer (id do gerente)
                Integer gerenteId = colaboradorId.intValue();

                // Contagem de membros da equipe
                List<Colaborador> equipe = gerenteService.listarColaboradoresPorSetorGerente(gerenteId);
                countMembrosDaEquipe = equipe.size();

                // Contagem de solicitações pendentes
                countSolicitacoesPendentes = gerenteService.contarSolicitacoesPendentesPorSetorDoGerente(gerenteId);

                // Se quiser, aqui também poderia buscar cargos, eventos etc.
            }
        }

        // Adiciona os valores ao modelo para uso no HTML
        model.addAttribute("countMembrosDaEquipe", countMembrosDaEquipe);
        model.addAttribute("countSolicitacoesPendentes", countSolicitacoesPendentes);
        model.addAttribute("countTotalCargos", countTotalCargos);
        model.addAttribute("countEventosProximos", countEventosProximos);

        return "gerentepages/dashboard";
    }

    @GetMapping("/solicitacoes")
    public String solicitacoes(Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        Integer gerenteId = null;

        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        if (colaboradorIdObj instanceof Integer) {
            gerenteId = (Integer) colaboradorIdObj;
        } else if (colaboradorIdObj instanceof Long) {
            gerenteId = ((Long) colaboradorIdObj).intValue();
        }

        List<Solicitacoes> solicitacoes = gerenteService.listarSolicitacoesPorSetorDoGerente(gerenteId);

        // Mapeia colaboradorId -> cargo
        Map<Integer, String> cargosPorColaborador = new HashMap<>();

        for (Solicitacoes sol : solicitacoes) {
            Colaborador colaborador = sol.getColaborador();
            if (colaborador != null && colaborador.getContrato() != null
                    && colaborador.getContrato().getCargos() != null) {
                String nomeCargo = colaborador.getContrato().getCargos().getNomeCargo();
                cargosPorColaborador.put(colaborador.getId(), nomeCargo);
            } else {
                cargosPorColaborador.put(colaborador.getId(), "Cargo não atribuído");
            }
        }

        model.addAttribute("solicitacoes", solicitacoes);
        model.addAttribute("cargos", cargosPorColaborador);
        model.addAttribute("solicitacao", new Solicitacoes());

        return "gerentepages/solicitacoes";
    }

    @GetMapping("/escala")
    public String escala(Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        return "gerentepages/escala";
    }

    @GetMapping("/equipe")
    public String equipe(Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

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
                    dto.setTelefone(colaborador.getTelefone());

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
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        Long colaboradorId = UserSessionUtils.getIdUsuario(session);
        Colaborador colaborador = gerenteService.buscarColaboradorPorId(colaboradorId);

        model.addAttribute("colaborador", colaborador);

        return "gerentepages/minhaconta";
    }

    @GetMapping("/alterarsenha")
    public String mostrarAlterarSenha(HttpSession session, Model model) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

        Long colaboradorId = UserSessionUtils.getIdUsuario(session);

        Colaborador colaborador = gerenteService.buscarColaboradorPorId(colaboradorId);
        PasswordChangeDTO passwordChangeDto = new PasswordChangeDTO();
        passwordChangeDto.setEmail(colaborador.getEmail());
        model.addAttribute("passwordChangeDto", passwordChangeDto);

        return "gerentepages/alterarsenha";
    }

    @PostMapping("/alterarsenha")
    public String alterarSenha(@ModelAttribute PasswordChangeDTO passwordChangeDto, Model model, HttpSession session) {
        if (!verifyIsUserCredentialsCorrect(session)) {
            return "redirect:" + LoginController.LOGIN_ROUTE;
        }

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

    @PostMapping("/solicitacoes/aprovar/{id}")
    @ResponseBody
    public ResponseEntity<String> aprovarSolicitacao(@PathVariable Integer id) {
        gerenteService.aprovarSolicitacao(id);
        return ResponseEntity.ok("Aprovado");
    }

    @PostMapping("/solicitacoes/recusar/{id}")
    @ResponseBody
    public ResponseEntity<String> recusarSolicitacao(@PathVariable Integer id) {
        gerenteService.recusarSolicitacao(id);
        return ResponseEntity.ok("Reprovado");
    }

}