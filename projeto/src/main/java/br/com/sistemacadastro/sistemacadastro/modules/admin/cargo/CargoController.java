package br.com.sistemacadastro.sistemacadastro.modules.admin.cargo;

import br.com.sistemacadastro.sistemacadastro.modules.admin.setor.Setores;
import br.com.sistemacadastro.sistemacadastro.modules.admin.setor.SetoresRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/cargo")
public class CargoController {

    @Autowired
    private CargoService cargoService;

    @Autowired
    private SetoresRepository setoresRepository;

    @GetMapping("/cadastrar")
    public String showCadastrarPageCargo(Model model) {
        model.addAttribute("cargosDto", new CargosDto());
        model.addAttribute("setores", setoresRepository.findAll());
        return "adminpages/cadastroCargo";
    }

    @PostMapping("/cadastrar")
    public String cadastrarCargos(@Valid @ModelAttribute CargosDto cargosDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("setores", setoresRepository.findAll());
            return "adminpages/cadastroCargo";
        }

        try {
            cargoService.cadastrarCargo(cargosDto);
            return "redirect:/admin/setorcargo";
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
    public String updateCargo(@Valid @ModelAttribute CargosDto cargosDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("setores", setoresRepository.findAll());
            return "adminpages/EditCargo";
        }

        cargoService.editarCargo(cargosDto.getId(), cargosDto);
        return "redirect:/admin/setorcargo";
    }

    @GetMapping("/deletar")
    public String deleteCargo(@RequestParam int id) {
        try {
            cargoService.excluirCargo(id);
            return "redirect:/admin/setorcargo";
        } catch (IllegalStateException e) {
            String error = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/admin/setorcargo?error=" + error;
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/admin/setorcargo?error=" +
                    URLEncoder.encode("Erro ao excluir cargo", StandardCharsets.UTF_8);
        }
    }
}
