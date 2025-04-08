package br.com.sistemacadastro.sistemacadastro.modules.Setores.repository;

import br.com.sistemacadastro.sistemacadastro.modules.Setores.model.Setores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetoresRepository extends JpaRepository<Setores, String> {
    Setores findById(long id);
}
