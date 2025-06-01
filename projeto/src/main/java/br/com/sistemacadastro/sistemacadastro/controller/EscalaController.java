package br.com.sistemacadastro.sistemacadastro.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sistemacadastro.sistemacadastro.model.Turnos;
import br.com.sistemacadastro.sistemacadastro.service.EscalaService;

@Controller
@RequestMapping("/escala")
public class EscalaController {

    @Autowired
    private EscalaService escalaService;

    @PostMapping("/gerarescala")
    public String gerarEscalaSemanal(@RequestParam("setorId") int setorId, RedirectAttributes redirectAttributes) {

        // Chama o service para gerar as escalas
        escalaService.gerarEscalasSemanais();

        redirectAttributes.addFlashAttribute("mensagem", "Escala gerada com sucesso para o setor ID: " + setorId);
        return "redirect:/admin/escala";
    }

    @PostMapping("/modificar")
    public String alterarTurnoEscala(
            @RequestParam Integer colaboradorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataEscala,
            @RequestParam(required = false) String turnoId,
            RedirectAttributes redirectAttributes) {

        Turnos turno = null;

        // Trata caso de folga (turnoId null, vazio ou "folga")
        if (turnoId != null && !turnoId.trim().isEmpty() && !turnoId.equalsIgnoreCase("folga")) {
            try {
                int id = Integer.parseInt(turnoId);
                if (id > 0) {
                    turno = new Turnos();
                    turno.setId(id);
                }
            } catch (NumberFormatException e) {
                System.out.println("ID de turno inválido: " + turnoId);
            }
        } else {
            System.out.println("Folga selecionada.");
        }

        boolean alterado = escalaService.alterarTurnoEscala(colaboradorId, dataEscala, turno);

        if (alterado) {
            redirectAttributes.addFlashAttribute("msgSucesso", "Turno alterado com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("msgErro", "Não foi possível alterar o turno.");
        }

        return "redirect:/admin/escala";
    }

    @PostMapping("/enviar")
    public String enviarEscalaParaRevisao(@RequestParam("setorId") Integer setorId, RedirectAttributes redirectAttributes) {
        try {
            escalaService.revisarEscalasSemanaSetor(setorId);
            redirectAttributes.addFlashAttribute("msgSucesso", "Escala enviada para o gerente do setor com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msgErro", "Erro ao enviar escala para revisão.");
        }
        return "redirect:/admin/escala?setorId=" + setorId;
    }

    @PostMapping("/aprovar")
    public String aprovarEscala(@RequestParam("setorId") Integer setorId, RedirectAttributes redirectAttributes) {
        try {
            escalaService.aprovarEscalasSemanaSetor(setorId);
            redirectAttributes.addFlashAttribute("msgSucesso", "Escala enviada para os operadores com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("msgErro", "Erro ao enviar escala para os operadores.");
        }
        return "redirect:/gerente/escala";
    }

}
