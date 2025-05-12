package br.com.sistemacadastro.sistemacadastro.modules.admin.setor;

import br.com.sistemacadastro.sistemacadastro.modules.admin.contrato.ContratoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/setores")
public class SetoresController {

    @Autowired
    private SetoresService setoresService;

    @GetMapping("")
    public String home() {
        return "setores";
    }

    @GetMapping("/cadastrar")
    public String showCadastrarPage(Model model) {
        SetoresDto setoresDto = new SetoresDto();
        model.addAttribute("setoresDto", setoresDto);
        return "adminpages/cadastroSetor";
    }

    @PostMapping("/cadastrar")
    public String cadastrarSetores(@Valid @ModelAttribute SetoresDto setoresDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "adminpages/cadastroSetor";
        }

        setoresService.cadastrarSetor(setoresDto);
        return "redirect:/admin/setorcargo";
    }

    @GetMapping("/editar/{id}")
    public String showEditPage(Model model, @PathVariable("id") int id) {
        SetoresDto setoresDto = setoresService.prepararEdicao(id);
        model.addAttribute("setoresDto", setoresDto);
        return "adminpages/EditSetores";
    }

    @PostMapping("/editar")
    public String updateSetor(Model model, @RequestParam int id, @Valid @ModelAttribute SetoresDto setoresDto, BindingResult result) {
        if (result.hasErrors()) {
            return "adminpages/EditSetores";
        }

        setoresService.editarSetor(id, setoresDto);
        return "redirect:/admin/setorcargo";
    }

    @GetMapping("/deletar")
    public String deleteSetores(@RequestParam int id, Model model) {
        boolean sucesso = setoresService.excluirSetor(id);

        if (!sucesso) {
            model.addAttribute("error", "Não é possível excluir o setor pois existem cargos associados.");
        }

        return "redirect:/admin/setorcargo";
    }
}
