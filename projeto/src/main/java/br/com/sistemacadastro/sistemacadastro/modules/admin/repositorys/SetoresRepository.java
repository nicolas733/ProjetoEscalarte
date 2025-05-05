package br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Setores;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SetoresRepository extends JpaRepository<Setores, String> {
    Setores findById(long id);
    Optional<Setores> findByNomesetor(String nomesetor);
}
