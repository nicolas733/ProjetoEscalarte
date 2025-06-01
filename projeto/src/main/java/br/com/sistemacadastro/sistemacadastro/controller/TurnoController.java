package br.com.sistemacadastro.sistemacadastro.controller;

import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Turnos;
import br.com.sistemacadastro.sistemacadastro.repository.TurnosRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/turnos")
public class TurnoController {

    @Autowired
    TurnosRepository turnosRepository;

    @GetMapping("/cadastrar")
    public String mostrarPagCadastro(Model model) {
        model.addAttribute("turno", new Turnos());
        return "adminpages/cadastroTurno";
    }

    @PostMapping("/cadastrar")
    public String cadastrarTurno(Model model, @Valid @ModelAttribute("turno") Turnos turno, BindingResult result) {
        Optional<Turnos> turnoOptional = turnosRepository.findByNome(turno.getNome());
        if (result.hasErrors()) {
            return "adminpages/cadastroTurno";
        }
        if (turnoOptional.isEmpty()) {
            turnosRepository.save(turno);
            return "redirect:/admin/turnos";
        } else {
            model.addAttribute("turnoJaCadastrado", true);
            return "adminpages/cadastroTurno";
        }
    }

    @GetMapping("/excluir")
    public String excluirTurno(@RequestParam int id) {
        try {
            Optional<Turnos> optionalTurno = turnosRepository.findById(id);
            if (optionalTurno.isPresent()) {
                turnosRepository.delete(optionalTurno.get());
            } else {
                System.out.println("Turno não encontrado para exclusão, id: " + id);
            }
        } catch (Exception ex) {
            System.out.println("Erro ao excluir turno: " + ex.getMessage());
        }

        return "redirect:/admin/turnos?excluido=true";
    }

}
