package br.com.sistemacadastro.sistemacadastro.modules.admin.colaborador;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ColaboradorRepository extends JpaRepository<Colaborador, String>{
    /*Procurando pelo id para validar o login*/
    Colaborador findById(long id);
    List<Colaborador> findAll();

    public Colaborador findFirstByEmailAndSenha(String email, String senha);

    Colaborador findCollaboratorById(Long id);

    Colaborador findCollaboratorByEmail(String email);



    Optional<Colaborador> findByEmail(String email);
}
