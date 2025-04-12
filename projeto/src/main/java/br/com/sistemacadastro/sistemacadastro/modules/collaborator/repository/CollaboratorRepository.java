package br.com.sistemacadastro.sistemacadastro.modules.collaborator.repository;

import br.com.sistemacadastro.sistemacadastro.modules.collaborator.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CollaboratorRepository extends JpaRepository<Collaborator, String>{
    /*Procurando pelo id para validar o login*/
    Collaborator findById(long id);

    public Collaborator findFirstByEmailAndSenha(String email, String senha);
}
