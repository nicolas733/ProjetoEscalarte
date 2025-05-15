package br.com.sistemacadastro.sistemacadastro.modules.admin.setor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.Colaborador;
import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.ColaboradorRepository;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/setores")
public class SetoresController {

    @Autowired
    private SetoresService setoresService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @GetMapping("")
    public String home() {
        return "setores";
    }

    @GetMapping("/cadastrar")
    public String showCadastrarPage(Model model) {
        SetoresDto setoresDto = new SetoresDto();
        List<Colaborador> colaboradoresList = colaboradorRepository.findAll();
        model.addAttribute("setoresDto", setoresDto);
        model.addAttribute("colaboradores", colaboradoresList);
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
        List<Colaborador> colaboradoresList = colaboradorRepository.findAll();
        model.addAttribute("setoresDto", setoresDto);
        model.addAttribute("colaboradores", colaboradoresList);
        model.addAttribute("id", id);
        return "adminpages/EditSetores";
    }

    @PostMapping("/editar")
    public String updateSetor(Model model, @Valid @ModelAttribute SetoresDto setoresDto, BindingResult result) {
        if (result.hasErrors()) {
            List<Colaborador> colaboradoresList = colaboradorRepository.findAll();
            model.addAttribute("colaboradores", colaboradoresList);
            return "adminpages/EditSetores";
        }

        setoresService.editarSetor(setoresDto.getId(), setoresDto);
        return "redirect:/admin/setorcargo";
    }

    @GetMapping("/deletar")
    public String deleteSetores(@RequestParam int id, RedirectAttributes redirectAttributes) {
        boolean sucesso = setoresService.excluirSetor(id);

        if (!sucesso) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir o setor pois existem cargos associados.");
        } else {
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Setor excluído com sucesso.");
        }

        return "redirect:/admin/setorcargo";
    }
}
