package br.com.sistemacadastro.sistemacadastro.cargos.controller;

import br.com.sistemacadastro.sistemacadastro.Setores.model.Setores;
import br.com.sistemacadastro.sistemacadastro.Setores.model.SetoresDto;
import br.com.sistemacadastro.sistemacadastro.Setores.repository.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.cargos.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.cargos.model.CargosDto;
import br.com.sistemacadastro.sistemacadastro.cargos.repository.CargoRepository;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/cargo")
public class CargoController {

    @Autowired
    private CargoRepository repo;

    @GetMapping("/cadastrarCargo")
    public String showCadastrarPage(Model model) {
        CargosDto cargosDto = new CargosDto();
        model.addAttribute("cargosDto", cargosDto);
        return "cadastroCargo"; // Retorna o template correto diretamente
    }

    @PostMapping("/cadastrarCargo")
    public String cadastrarSetores(@Valid @ModelAttribute CargosDto cargosDto, BindingResult result) {
        if (result.hasErrors()) {
            return "cadastroCargo";
        }
        Cargos cargos = new Cargos();
        cargos.setNomeCargo(cargosDto.getNomeCargo());
        cargos.setCargaHorarioLimite(cargosDto.getCargoHorarioLimite());
        repo.save(cargos);

        return "redirect:/cargo/main";
    }

    @GetMapping({"/main"})
    public String listarCargos(Model model) {
        List<Cargos> cargos = repo.findAll();
        model.addAttribute("cargos", cargos);
        return "home";
    }


    @GetMapping("/editcargo/{id}")
    public String showEditPage(Model model, @PathVariable("id") int id) {
        try {
            Cargos cargos = repo.findById(id);  // Verifique se 'repo.findById(id)' retorna um colaborador válido.
            model.addAttribute("cargos", cargos);

            CargosDto cargosDto = new CargosDto();
            cargosDto.setNomeCargo(cargos.getNomeCargo());
            cargosDto.setCargoHorarioLimite(cargos.getCargaHorarioLimite());

            model.addAttribute("cargosDto", cargosDto);
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/cargo/main";
        }

        return "EditCargo";
    }


    @PostMapping("/editcargo")
    public String updateCargo(Model model, @RequestParam int id,@Valid @ModelAttribute CargosDto cargosDto, BindingResult result) {
        try{
            Cargos cargos = repo.findById(id);
            System.out.println(cargos);
            model.addAttribute("cargos", cargos);

            if (result.hasErrors()) {
                return "EditCargo";
            }
            cargos.setNomeCargo(cargosDto.getNomeCargo());
            cargos.setCargaHorarioLimite(cargosDto.getCargoHorarioLimite());
            System.out.println(cargos);
            repo.save(cargos);

        }catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        return "redirect:/cargo/main";
    }

    @GetMapping("/deletecargo")
    public String deleteCargo(@RequestParam int id) {
        try{
            Cargos cargos = repo.findById(id);
            repo.delete(cargos);
        }catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }

        return "redirect:/cargo/main";
    }
}
