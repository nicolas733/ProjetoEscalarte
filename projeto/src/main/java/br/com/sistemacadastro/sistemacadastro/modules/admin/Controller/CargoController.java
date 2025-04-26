package br.com.sistemacadastro.sistemacadastro.modules.admin.Controller;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Cargos;
import br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs.CargosDto;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.CargoRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/cargo")
public class CargoController{

    @Autowired
    private CargoRepository repository;


    @GetMapping("/cadastrarCargo")
    public String showCadastrarPageCargo(Model model) {
        CargosDto cargosDto = new CargosDto();
        model.addAttribute("cargosDto", cargosDto);
        return "adminpages/cadastroCargo"; // Retorna o template correto diretamente
    }

    @PostMapping("/cadastrarCargo")
    public String cadastrarCargos(@Valid @ModelAttribute CargosDto cargosDto, BindingResult result) {
        if (result.hasErrors()) {
            return "adminpages/cadastroCargo";
        }
        Cargos cargos = new Cargos();
        cargos.setNomeCargo(cargosDto.getNomeCargo());
        cargos.setCargaHorarioLimite(cargosDto.getCargoHorarioLimite());
        repository.save(cargos);

        return "redirect:/admin/setorcargo";
    }


    @GetMapping("/editcargo/{id}")
    public String showEditPageCargos(Model model, @PathVariable("id") int id) {
        try {
            Cargos cargos = repository.findById(id);  // Verifique se 'repo.findById(id)' retorna um colaborador válido.
            model.addAttribute("cargos", cargos);

            CargosDto cargosDto = new CargosDto();
            cargosDto.setNomeCargo(cargos.getNomeCargo());
            cargosDto.setCargoHorarioLimite(cargos.getCargaHorarioLimite());

            model.addAttribute("cargosDto", cargosDto);
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/admin/setorcargo";
        }

        return "adminpages/EditCargo";
    }


    @PostMapping("/editcargo")
    public String updateCargo(Model model, @RequestParam int id, @Valid @ModelAttribute CargosDto cargosDto, BindingResult result) {
        try{
            Cargos cargos = repository.findById(id);
            System.out.println(cargos);
            model.addAttribute("cargos", cargos);

            if (result.hasErrors()) {
                return "adminpages/EditCargo";
            }
            cargos.setNomeCargo(cargosDto.getNomeCargo());
            cargos.setCargaHorarioLimite(cargosDto.getCargoHorarioLimite());
            System.out.println(cargos);
            repository.save(cargos);

        }catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        return "redirect:/admin/setorcargo";
    }

    @GetMapping("/deletecargo")
    public String deleteCargo(@RequestParam int id) {
        try{
            Cargos cargos = repository.findById(id);
            repository.delete(cargos);
        }catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }

        return "redirect:/admin/setorcargo";
    }

}