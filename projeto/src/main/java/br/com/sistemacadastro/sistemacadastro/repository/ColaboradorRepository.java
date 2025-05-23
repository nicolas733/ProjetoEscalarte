package br.com.sistemacadastro.sistemacadastro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemacadastro.sistemacadastro.model.Colaborador;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Integer>{
    /*Procurando pelo id para validar o login*/
    Colaborador findById(long id);
    List<Colaborador> findAll();

   Colaborador findFirstByEmailAndSenha(String email, String senha);

    Colaborador findCollaboratorByEmail(String email);

    Optional<Colaborador> findByEmail(String email);

    List<Colaborador> findByTipoUsuario(Colaborador.TipoUsuario tipoUsuario);
}
