package br.com.sistemacadastro.sistemacadastro.util;

import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador.TipoUsuario;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    public static boolean isLogged(HttpSession session) {
        return session != null && session.getAttribute("colaboradorId") != null;
    }

    public static Long getIdUsuario(HttpSession session) {
        if (!isLogged(session))
            return null;
        Object colaboradorIdObj = session.getAttribute("colaboradorId");
        Long colaboradorId = ((Number) colaboradorIdObj).longValue();
        return colaboradorId;
    }

    public static String getNomeUsuario(HttpSession session) {
        if (!isLogged(session))
            return null;
        Object usuarioNome = session.getAttribute("colaboradorNome");
        return usuarioNome != null ? usuarioNome.toString() : null;
    }

    public static TipoUsuario getTipoUsuario(HttpSession session) {
        if (!isLogged(session))
            return null;
        Object usuarioTipo = session.getAttribute("colaboradorTipoUsuario");
        return usuarioTipo != null ? TipoUsuario.valueOf(usuarioTipo.toString()) : null;
    }

    public static void setUsuario(HttpSession session, Colaborador usuario) {
        if (session != null) {
            session.setAttribute("colaboradorId", usuario.getId());
            session.setAttribute("colaboradorNome", usuario.getNome());
            session.setAttribute("colaboradorTipoUsuario", usuario.getTipoUsuario());
        }
    }

    public static boolean isAdmin(HttpSession session) {
        return getTipoUsuario(session) == TipoUsuario.ADMIN;
    }

    public static boolean isGerente(HttpSession session) {
        return getTipoUsuario(session) == TipoUsuario.GERENTE;
    }

    public static boolean isOperador(HttpSession session) {
        return getTipoUsuario(session) == TipoUsuario.OPERADOR;
    }
}
