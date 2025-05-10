package br.com.sistemacadastro.sistemacadastro.modules.admin.cargosPorSetor;

import br.com.sistemacadastro.sistemacadastro.modules.admin.cargo.Cargos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargosPorSetorRepository extends JpaRepository<CargosPorSetor, Integer> {
    // Busca a associação entre Cargo e Setor usando o Cargo
    CargosPorSetor findByCargo(Cargos cargos);

    // Busca a associação entre Cargo e Setor usando o ID do Cargo
    CargosPorSetor findByCargoId(int cargoId);
}
