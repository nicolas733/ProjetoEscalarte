package br.com.sistemacadastro.sistemacadastro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemacadastro.sistemacadastro.cargo.Cargos;
import br.com.sistemacadastro.sistemacadastro.cargosPorSetor.CargosPorSetor;

public interface CargosPorSetorRepository extends JpaRepository<CargosPorSetor, Integer> {
    // Busca a associação entre Cargo e Setor usando o Cargo
    CargosPorSetor findByCargo(Cargos cargos);

    // Busca a associação entre Cargo e Setor usando o ID do Cargo
    CargosPorSetor findByCargoId(int cargoId);
}
