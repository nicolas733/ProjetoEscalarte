package br.com.sistemacadastro.sistemacadastro.Setores.repository;

import br.com.sistemacadastro.sistemacadastro.Setores.model.Setores;
import br.com.sistemacadastro.sistemacadastro.collaborator.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SetoresRepository extends JpaRepository<Setores, String> {
    Setores findById(long id);
}
