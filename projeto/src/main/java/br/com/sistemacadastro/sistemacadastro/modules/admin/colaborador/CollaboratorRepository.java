package br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CollaboratorRepository extends JpaRepository<Collaborator, String>{
    /*Procurando pelo id para validar o login*/
    Collaborator findById(long id);

    public Collaborator findFirstByEmailAndSenha(String email, String senha);

    Collaborator findCollaboratorById(Long id);

    Collaborator findCollaboratorByEmail(String email);



    Optional<Collaborator> findByEmail(String email);
}
