package br.com.sistemacadastro.sistemacadastro.modules;

import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.Collaborator;
import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.CollaboratorRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class LoginController {

    @Autowired
    private CollaboratorRepository repo;


    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @PostMapping("/logar")
    public String logar(Collaborator collaborator, Model model, HttpServletResponse response, HttpSession session) {
        Collaborator collaboratorLogado = this.repo.findFirstByEmailAndSenha(collaborator.getEmail(), collaborator.getSenha());
        if (collaboratorLogado != null) {
            session.setAttribute("colaboradorId", collaboratorLogado.getId());
            session.setAttribute("colaboradorNome", collaboratorLogado.getNome());

            Collaborator.UserType tipo = collaboratorLogado.getUserType();
            if (tipo.equals(Collaborator.UserType.ADMIN)) {
                return "redirect:/admin/dashboard";
            } else if (tipo.equals(Collaborator.UserType.GERENTE)) {
                return "redirect:/gerente/dashboard";
            } else if (tipo.equals(Collaborator.UserType.OPERADOR)) {
                return "redirect:/operador/dashboard";
            }
        }
        model.addAttribute("erro", "Usuario invalido");
        return "login";
    }





    @GetMapping("")
    public String home() {
        return "usuarios";
    }


}


