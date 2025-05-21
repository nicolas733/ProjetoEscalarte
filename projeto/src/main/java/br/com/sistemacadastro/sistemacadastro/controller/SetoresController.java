package br.com.sistemacadastro.sistemacadastro.controller;

import java.util.List;
import java.util.Optional;

import br.com.sistemacadastro.sistemacadastro.model.Setores;
import br.com.sistemacadastro.sistemacadastro.repository.SetoresRepository;
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

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.sistemacadastro.sistemacadastro.dto.SetoresDTO;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.service.SetoresService;

@Controller
@RequestMapping("/setores")
public class SetoresController {

    @Autowired
    private SetoresService setoresService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private SetoresRepository setoresRepository;

    @GetMapping("")
    public String home() {
        return "setores";
    }

    @GetMapping("/cadastrar")
    public String showCadastrarPage(Model model) {
        SetoresDTO setoresDto = new SetoresDTO();
        List<Colaborador> colaboradoresList = colaboradorRepository.findAll();
        model.addAttribute("setoresDto", setoresDto);
        model.addAttribute("colaboradores", colaboradoresList);
        return "adminpages/cadastroSetor";
    }

    @PostMapping("/cadastrar")
    public String cadastrarSetores(@Valid @ModelAttribute("setoresDto") SetoresDTO setoresDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            // Repopula os colaboradores para o select
            List<Colaborador> colaboradoresList = colaboradorRepository.findAll();
            model.addAttribute("colaboradores", colaboradoresList);
            // setoresDto já está no model via @ModelAttribute
            return "adminpages/cadastroSetor";
        }

        Optional<Setores> setoresExistentes = setoresRepository.findByNomesetor(setoresDto.getNomeSetor());
        if (setoresExistentes.isEmpty()) {
            Setores setores = new Setores();
            setores.setNomesetor(setoresDto.getNomeSetor());
            setores.setQuantidadeColaboradores(setoresDto.getQuantidadeColaboradores());
            setoresRepository.save(setores);
        }else {
            model.addAttribute("setorJaCadastrado", true);
            return "adminpages/cadastroSetor";
        }
        return "redirect:/admin/setorcargo?sucesso=true";
    }

    @GetMapping("/editar/{id}")
    public String showEditPage(Model model, @PathVariable("id") int id) {
        SetoresDTO setoresDto = setoresService.prepararEdicao(id);
        List<Colaborador> operadoresList = colaboradorRepository.findByTipoUsuario(Colaborador.TipoUsuario.OPERADOR);
        model.addAttribute("setoresDto", setoresDto);
        model.addAttribute("colaboradores", operadoresList);
        model.addAttribute("id", id);
        return "adminpages/EditSetores";
    }

    @PostMapping("/editar")
    public String updateSetor(Model model, @Valid @ModelAttribute SetoresDTO setoresDto, BindingResult result) {
        if (result.hasErrors()) {
            List<Colaborador> colaboradoresList = colaboradorRepository.findAll();
            model.addAttribute("colaboradores", colaboradoresList);
            return "adminpages/EditSetores";
        }

        setoresService.editarSetor(setoresDto.getId(), setoresDto);
        return "redirect:/admin/setorcargo?editado=true";
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
