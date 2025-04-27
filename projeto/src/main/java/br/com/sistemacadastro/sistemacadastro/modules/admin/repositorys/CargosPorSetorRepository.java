package br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Cargos;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.CargosPorSetor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CargosPorSetorRepository extends JpaRepository<CargosPorSetor, Integer> {
    // Busca a associação entre Cargo e Setor usando o Cargo
    CargosPorSetor findByCargo(Cargos cargos);

    // Busca a associação entre Cargo e Setor usando o ID do Cargo
    CargosPorSetor findByCargoId(int cargoId);
}
