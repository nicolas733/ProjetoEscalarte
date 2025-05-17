package br.com.sistemacadastro.sistemacadastro.controller;

import br.com.sistemacadastro.sistemacadastro.dto.ColaboradorDTO;
import br.com.sistemacadastro.sistemacadastro.dto.EditDTO;
import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.service.ColaboradorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/colaborador")
public class ColaboradorController {

    @Autowired
    private ColaboradorService service;

    @GetMapping("/cadastrar")
    public String showCadastrarPage(Model model) {
        ColaboradorDTO colaboradorDto = new ColaboradorDTO();
        List<Cargos> cargos = service.listarCargos();
        model.addAttribute("cargos", cargos);
        model.addAttribute("colaboradorDto", colaboradorDto);
        return "adminpages/cadastroColaborador";
    }

    @PostMapping("/cadastrar")
    public String cadastrarColaborador(@Valid @ModelAttribute ColaboradorDTO colaboradorDto, BindingResult result, Model model) {
        List<Cargos> cargos = service.listarCargos();
        model.addAttribute("cargos", cargos);

        if (result.hasErrors()) {
            System.out.println("Erro de validação!");
            model.addAttribute("colaboradorDto", colaboradorDto);
            return "adminpages/cadastroColaborador";
        }

        Optional<Colaborador> colaboradorExistente = service.buscarPorEmail(colaboradorDto.getEmail());
        if (colaboradorExistente.isEmpty()) {
            service.salvarColaborador(colaboradorDto);
            return "redirect:/admin/main";
        } else {
            model.addAttribute("emailJaCadastrado", true);
            model.addAttribute("colaboradorDto", colaboradorDto);
            return "adminpages/cadastroColaborador";
        }
    }


    @GetMapping("/editar/{id}")
    public String mostrarPagEdicao(Model model, @PathVariable("id") int id) {
        try {
            Colaborador colaborador = service.buscarPorId(id);
            model.addAttribute("colaborador", colaborador);

            EditDTO editDto = new EditDTO();
            editDto.setId(colaborador.getId());
            editDto.setNome(colaborador.getNome());
            editDto.setEmail(colaborador.getEmail());
            editDto.setTelefone(colaborador.getTelefone());
            editDto.setCpf(colaborador.getCpf());
            editDto.setTipoUsuario(colaborador.getTipoUsuario());
            editDto.setEndereco(colaborador.getEndereco());
            editDto.setContrato(colaborador.getContrato());

            model.addAttribute("editDto", editDto);

        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/admin/main";
        }

        return "adminpages/EditColaborador";
    }

    @PostMapping("/editar")
    public String atualizarColaborador(Model model, @Valid @ModelAttribute EditDTO editDto, BindingResult result) {
        try {
            Colaborador colaborador = service.buscarPorId(editDto.getId());
            model.addAttribute("colaborador", colaborador);
            model.addAttribute("editDto", editDto);

            if (result.hasErrors()) {
                return "adminpages/EditColaborador";
            }

            service.atualizarColaborador(editDto);

        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        return "redirect:/admin/main";
    }

    @GetMapping("/deletar")
    public String excluirColaborador(@RequestParam int id) {
        try {
            service.deletarColaborador(id);
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }

        return "redirect:/admin/main";
    }
}
