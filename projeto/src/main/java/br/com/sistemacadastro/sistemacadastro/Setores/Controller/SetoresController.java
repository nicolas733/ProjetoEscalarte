package br.com.sistemacadastro.sistemacadastro.Setores.Controller;

import br.com.sistemacadastro.sistemacadastro.Setores.model.Setores;
import br.com.sistemacadastro.sistemacadastro.Setores.model.SetoresDto;
import br.com.sistemacadastro.sistemacadastro.Setores.repository.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.collaborator.model.Collaborator;
import br.com.sistemacadastro.sistemacadastro.collaborator.model.CollaboratorDto;
import br.com.sistemacadastro.sistemacadastro.collaborator.repository.CollaboratorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/setores")
public class SetoresController {

    @Autowired
    private SetoresRepository repo;

    @GetMapping("/cadastrarSetor")
    public String showCadastrarPage(Model model) {
        SetoresDto setoresDto = new SetoresDto();
        model.addAttribute("setoresDto", setoresDto);
        return "cadastroSe"; // Retorna o template correto diretamente
    }

    @PostMapping("/cadastrarSetor")
    public String cadastrarSetores(@Valid @ModelAttribute SetoresDto setoresDto, BindingResult result) {
        if (result.hasErrors()) {
            return "cadastroSe";
        }
        Setores setores = new Setores();
        setores.setNomesetor(setoresDto.getNomeSetor());
        setores.setQuantidadeColaboradores(setoresDto.getQuantidadeColaboradores());
        repo.save(setores);

        return "redirect:/setores/main";
    }

    @GetMapping({"/main"})
    public String listarSetores(Model model) {
        List<Setores> setores = repo.findAll();
        model.addAttribute("setores", setores);
        return "home";
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
            return "redirect:/setores/main";
        }

        return "EditSetores";
    }


    @PostMapping("/editsetor")
    public String updateCollaborator(Model model, @RequestParam int id,@Valid @ModelAttribute SetoresDto setoresDto, BindingResult result) {
        try{
            Setores setores = repo.findById(id);
            System.out.println(setores);
            model.addAttribute("setores", setores);

            if (result.hasErrors()) {
                return "EditSetores";
            }
            setores.setNomesetor(setoresDto.getNomeSetor());
            setores.setQuantidadeColaboradores(setoresDto.getQuantidadeColaboradores());
            System.out.println(setores);
            repo.save(setores);

        }catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }
        return "redirect:/setores/main";
    }

    @GetMapping("/deletesetor")
    public String deleteSetores(@RequestParam int id) {
        try{
            Setores setores = repo.findById(id);
            repo.delete(setores);
        }catch (Exception ex) {
            System.out.println("Erro: " + ex.getMessage());
        }

        return "redirect:/setores/main";
    }
}


