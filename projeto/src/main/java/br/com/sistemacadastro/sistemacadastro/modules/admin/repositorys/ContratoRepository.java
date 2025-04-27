package br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Cargos;
import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
    boolean existsByCargosAndAtivo(Cargos cargos, boolean ativo);

}
