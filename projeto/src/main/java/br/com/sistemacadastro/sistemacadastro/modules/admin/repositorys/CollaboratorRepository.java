package br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Collaborator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollaboratorRepository extends JpaRepository<Collaborator, String>{
    /*Procurando pelo id para validar o login*/
    Collaborator findById(long id);

    public Collaborator findFirstByEmailAndSenha(String email, String senha);


    Optional<Collaborator> findByEmail(String email);
}
