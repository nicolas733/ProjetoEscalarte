package br.com.sistemacadastro.sistemacadastro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemacadastro.sistemacadastro.colaborador.Colaborador;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Integer>{
    /*Procurando pelo id para validar o login*/
    Colaborador findById(long id);
    List<Colaborador> findAll();

    public Colaborador findFirstByEmailAndSenha(String email, String senha);

    Colaborador findCollaboratorById(Long id);

    Colaborador findCollaboratorByEmail(String email);



    Optional<Colaborador> findByEmail(String email);
}
