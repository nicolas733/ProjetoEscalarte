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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
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

        return "redirect:/main";
    }
}
