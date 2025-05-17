package br.com.sistemacadastro.sistemacadastro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemacadastro.sistemacadastro.cargo.Cargos;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<Cargos, Integer> {
    Cargos findById(long id);

    Optional<Cargos> findByNomeCargo(String nomeCargo);

}
