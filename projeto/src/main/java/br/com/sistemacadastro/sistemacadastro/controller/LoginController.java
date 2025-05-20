package br.com.sistemacadastro.sistemacadastro.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.JmsProperties.Listener.Session;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador.TipoUsuario;
import br.com.sistemacadastro.sistemacadastro.repository.ColaboradorRepository;
import br.com.sistemacadastro.sistemacadastro.util.SessionUtils;

@Controller
@RequestMapping("")
public class LoginController {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    private static String redirecionarParaDashboard(TipoUsuario tipoUsuario) {
        if (tipoUsuario.equals(Colaborador.TipoUsuario.ADMIN)) {
            return "redirect:/admin/dashboard";
        } else if (tipoUsuario.equals(Colaborador.TipoUsuario.GERENTE)) {
            return "redirect:/gerente/dashboard";
        } else if (tipoUsuario.equals(Colaborador.TipoUsuario.OPERADOR)) {
            return "redirect:/operador/dashboard";
        }
        return "erro";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (SessionUtils.isLogged(session)) {
            return redirecionarParaDashboard(SessionUtils.getTipoUsuario(session));
        }
        return "login";
    }

    @PostMapping("/login")
    public String logar(Colaborador colaborador, Model model, HttpServletResponse response, HttpSession session) {
        Colaborador colaboradorLogado = this.colaboradorRepository.findFirstByEmailAndSenha(colaborador.getEmail(),
                colaborador.getSenha());
        if (colaboradorLogado != null) {
            SessionUtils.setUsuario(session, colaboradorLogado);
            Colaborador.TipoUsuario tipo = colaboradorLogado.getTipoUsuario();

            return redirecionarParaDashboard(tipo);
        }

        model.addAttribute("erro", "Usuario invalido");
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login";
    }

}
