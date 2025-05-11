package br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ColaboradorRepository extends JpaRepository<Colaborador, String>{
    /*Procurando pelo id para validar o login*/
    Colaborador findById(long id);

    public Colaborador findFirstByEmailAndSenha(String email, String senha);

    Colaborador findCollaboratorById(Long id);

    Colaborador findCollaboratorByEmail(String email);



    Optional<Colaborador> findByEmail(String email);
}
