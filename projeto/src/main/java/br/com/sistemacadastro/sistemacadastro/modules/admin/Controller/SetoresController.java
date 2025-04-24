package br.com.sistemacadastro.sistemacadastro.modules.admin.Controller;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Setores;
import br.com.sistemacadastro.sistemacadastro.modules.admin.DTOs.SetoresDto;
import br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys.SetoresRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/setores")
public class SetoresController {

    @Autowired
    private SetoresRepository repo;


    @GetMapping("")
    public String home() {
        return "setores";
    }

    @GetMapping("/cadastrarSetor")
    public String showCadastrarPage(Model model) {
        SetoresDto setoresDto = new SetoresDto();
        model.addAttribute("setoresDto", setoresDto);
        return "adminpages/cadastroSe"; // Retorna o template correto diretamente
    }

    @PostMapping("/cadastrarSetor")
    public String cadastrarSetores(@Valid @ModelAttribute SetoresDto setoresDto, BindingResult result) {
        if (result.hasErrors()) {
            return "adminpages/cadastroSe";
        }
        Setores setores = new Setores();
        setores.setNomesetor(setoresDto.getNomeSetor());
        setores.setQuantidadeColaboradores(setoresDto.getQuantidadeColaboradores());
        repo.save(setores);

        return "redirect:/admin/setorcargo";
    }



    @GetMapping("/editsetor/{id}")
    public String showEditPage(Model model, @PathVariable("id") int id) {
        try {
            Setores setores = repo.findById(id);  // Verifique se 'repo.findById(id)' retorna um colaborador válido.
            model.addAttribute("setores", setores);

            SetoresDto setoresDto = new SetoresDto();
            setoresDto.setNomeSetor(setores.getNomesetor());
            setoresDto.setQuantidadeColaboradores(setores.getQuantidadeColaboradores());

            model.addAttribute("setoresDto", setoresDto);
        } catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
            return "redirect:/admin/setorcargo";
        }

        return "adminpages/EditSetores";
    }


    @PostMapping("/editsetor")
    public String updateCollaborator(Model model, @RequestParam int id,@Valid @ModelAttribute SetoresDto setoresDto, BindingResult result) {
        try{
            Setores setores = repo.findById(id);
            System.out.println(setores);
            model.addAttribute("setores", setores);

            if (result.hasErrors()) {
                return "adminpages/EditSetores";
            }
            setores.setNomesetor(setoresDto.getNomeSetor());
            setores.setQuantidadeColaboradores(setoresDto.getQuantidadeColaboradores());
            System.out.println(setores);
            repo.save(setores);

        }catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        return "redirect:/admin/setorcargo";
    }

    @GetMapping("/deletesetor")
    public String deleteSetores(@RequestParam int id) {
        try{
            Setores setores = repo.findById(id);
            repo.delete(setores);
        }catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }

        return "redirect:/admin/setorcargo";
    }
}


