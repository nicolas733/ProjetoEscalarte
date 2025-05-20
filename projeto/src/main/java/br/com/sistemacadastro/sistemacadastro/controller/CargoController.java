package br.com.sistemacadastro.sistemacadastro.controller;

import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.repository.CargoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sistemacadastro.sistemacadastro.dto.CargosDTO;
import br.com.sistemacadastro.sistemacadastro.model.Setores;
import br.com.sistemacadastro.sistemacadastro.repository.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.service.CargoService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/cargo")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @Autowired
    private SetoresRepository setoresRepository;

    @Autowired
    private CargoRepository cargoRepository;

    @GetMapping("/cadastrar")
    public String showCadastrarPageCargo(Model model) {
        model.addAttribute("cargosDTO", new CargosDTO());
        model.addAttribute("setores", setoresRepository.findAll());
        return "adminpages/cadastroCargo";
    }

    @PostMapping("/cadastrar")
    public String cadastrarCargos(@Valid @ModelAttribute CargosDTO cargosDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("setores", setoresRepository.findAll());
            model.addAttribute("cargosDTO", cargosDto);
            return "adminpages/cadastroCargo";
        }
        Optional<Cargos> existente = cargoRepository.findByNomeCargo(cargosDto.getNomeCargo());
        if (existente.isPresent()) {
            model.addAttribute("cargoJaCadastrado", true);
            return "adminpages/cadastroCargo";
        }

        try {
            cargoService.cadastrarCargo(cargosDto);
            return "redirect:/admin/setorcargo?sucesso=true";
        } catch (IllegalArgumentException e) {
            model.addAttribute("nomeJaCadastrado", true);
            model.addAttribute("setores", setoresRepository.findAll());
            return "adminpages/cadastroCargo";
        }
    }

    @GetMapping("/editar/{id}")
    public String showEditPageCargos(Model model, @PathVariable("id") int id) {
        return cargoService.prepararEdicao(id, model);
    }

    @PostMapping("/editar")
    public String updateCargo(@Valid @ModelAttribute CargosDTO cargosDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("setores", setoresRepository.findAll());
            return "adminpages/EditCargo";
        }

        cargoService.editarCargo(cargosDto.getId(), cargosDto);
        return "redirect:/admin/setorcargo?editado=true";
    }

    @GetMapping("/deletar")
    public String deleteCargo(@RequestParam int id, RedirectAttributes redirectAttributes) {
        try {
            cargoService.excluirCargo(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Cargo excluído com sucesso.");
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("mensagemErro", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir cargo.");
        }
        return "redirect:/admin/setorcargo";
    }

}
