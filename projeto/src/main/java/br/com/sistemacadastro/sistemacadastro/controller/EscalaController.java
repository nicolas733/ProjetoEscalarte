package br.com.sistemacadastro.sistemacadastro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
}
