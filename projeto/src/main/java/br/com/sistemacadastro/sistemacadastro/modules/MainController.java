package br.com.sistemacadastro.sistemacadastro.modules;


import br.com.sistemacadastro.sistemacadastro.modules.Setores.model.Setores;
import br.com.sistemacadastro.sistemacadastro.modules.Setores.repository.SetoresRepository;
import br.com.sistemacadastro.sistemacadastro.modules.cargos.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.modules.cargos.model.CargosDto;
import br.com.sistemacadastro.sistemacadastro.modules.cargos.repository.CargoRepository;
import br.com.sistemacadastro.sistemacadastro.modules.collaborator.model.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.collaborator.model.CollaboratorDto;
import br.com.sistemacadastro.sistemacadastro.modules.collaborator.repository.CollaboratorRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class MainController {

    @Autowired
    private CollaboratorRepository repo;

    @Autowired
    private CargoRepository repository;

    @Autowired
    private SetoresRepository reposito;


    @GetMapping({"/main"})
    public String listarDados(Model model) {
        List<Collaborator> collaborators = repo.findAll();
        List<Setores> setores = reposito.findAll();
        List<Cargos> cargos = repository.findAll();
        model.addAttribute("collaborators", collaborators);
        model.addAttribute("setores", setores);
        model.addAttribute("cargos", cargos);
        return "home";
    }

    @GetMapping("")
    public String home() {
        return "home";
    }

}

