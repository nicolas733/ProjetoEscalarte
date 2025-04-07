package br.com.sistemacadastro.sistemacadastro.cargos.repository;

import br.com.sistemacadastro.sistemacadastro.cargos.model.Cargos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargoRepository extends JpaRepository<Cargos, String> {
    Cargos findById(long id);
}
