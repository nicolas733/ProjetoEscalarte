package br.com.sistemacadastro.sistemacadastro.modules;

import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.Colaborador;
import br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador.ColaboradorRepository;
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
    private ColaboradorRepository repo;


    @GetMapping("/login")
    public String login() {
        return "login";
    }


    @PostMapping("/logar")
    public String logar(Colaborador colaborador, Model model, HttpServletResponse response, HttpSession session) {
        Colaborador colaboradorLogado = this.repo.findFirstByEmailAndSenha(colaborador.getEmail(), colaborador.getSenha());
        if (colaboradorLogado != null) {
            session.setAttribute("colaboradorId", colaboradorLogado.getId());
            session.setAttribute("colaboradorNome", colaboradorLogado.getNome());

            Colaborador.TipoUsuario tipo = colaboradorLogado.getTipoUsuario();
            if (tipo.equals(Colaborador.TipoUsuario.ADMIN)) {
                return "redirect:/admin/dashboard";
            } else if (tipo.equals(Colaborador.TipoUsuario.GERENTE)) {
                return "redirect:/gerente/dashboard";
            } else if (tipo.equals(Colaborador.TipoUsuario.OPERADOR)) {
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


