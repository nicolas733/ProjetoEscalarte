package br.com.sistemacadastro.sistemacadastro.modules.collaborator.repository;

import br.com.sistemacadastro.sistemacadastro.modules.collaborator.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CollaboratorRepository extends JpaRepository<Collaborator, String>{
    /*Procurando pelo id para validar o login*/
    Collaborator findById(long id);

    @Query(value = "select * from collaborator where email = :email and senha = :senha and typeuser = 1", nativeQuery = true)
    public Collaborator login(String email, String senha);
}
