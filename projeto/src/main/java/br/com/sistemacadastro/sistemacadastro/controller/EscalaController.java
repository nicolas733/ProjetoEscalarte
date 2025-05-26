package br.com.sistemacadastro.sistemacadastro.controller;

import br.com.sistemacadastro.sistemacadastro.dto.EscalaDTO;
import br.com.sistemacadastro.sistemacadastro.model.Escalas;
import br.com.sistemacadastro.sistemacadastro.model.Setores;
import br.com.sistemacadastro.sistemacadastro.repository.EscalaRepository;
import br.com.sistemacadastro.sistemacadastro.repository.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.service.GeradorEscalaService;
import br.com.sistemacadastro.sistemacadastro.service.RegrasCLTService;
import br.com.sistemacadastro.sistemacadastro.service.SetoresService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.sql.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/escala")
public class EscalaController {

    @Autowired
    private RegrasCLTService regrasCLTService;

    @Autowired
    private EscalaRepository escalaRepository;

    @Autowired
    private GeradorEscalaService geradorEscalaService;

    @Autowired
    private SetoresRepository setoresRepository;  // injeta o serviço de setores


    // Método para gerar escala semanal por setor
    @PostMapping("/gerarescala")
    public String gerarEscalaSemanal(@RequestParam("setorId") int setorId, RedirectAttributes redirectAttributes) {
        Date hoje = Date.valueOf(LocalDate.now());
        Date seteDiasDepois = Date.valueOf(LocalDate.now().plusDays(7));

        // Chama o service para gerar as escalas
        List<Escalas> escalasGeradas = geradorEscalaService.gerarEscalasSemanais();

        // Salva no banco
        escalaRepository.saveAll(escalasGeradas);

        redirectAttributes.addFlashAttribute("mensagem", "Escala gerada com sucesso para o setor ID: " + setorId);
        return "redirect:/admin/escala";
    }
}

