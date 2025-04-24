package br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Cargos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargos, String> {
    Cargos findById(long id);
}
