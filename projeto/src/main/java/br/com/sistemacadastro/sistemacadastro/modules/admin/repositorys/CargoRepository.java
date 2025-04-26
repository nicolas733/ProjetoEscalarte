package br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Cargos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargos, Integer> {
    Cargos findById(long id);

    Cargos findByNomeCargo(String nomeCargo);
}
