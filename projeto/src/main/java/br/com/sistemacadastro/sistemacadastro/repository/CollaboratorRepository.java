package br.com.sistemacadastro.sistemacadastro.repository;

import br.com.sistemacadastro.sistemacadastro.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CollaboratorRepository extends JpaRepository<Collaborator, String>{
    /*Procurando pelo id para validar o login*/
    Collaborator findById(long id);

    @Query(value = "select * from collaborator where email = :email and senha = :senha", nativeQuery = true)
    public Collaborator login(String email, String senha);

    long id(long id);
}
