package br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Setores;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SetoresRepository extends JpaRepository<Setores, String> {
    Setores findById(long id);
}
