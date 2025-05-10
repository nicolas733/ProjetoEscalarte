package br.com.sistemacadastro.sistemacadastro.modules.admin.contrato;

import br.com.sistemacadastro.sistemacadastro.modules.admin.cargo.Cargos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
    boolean existsByCargosAndAtivo(Cargos cargos, boolean ativo);

}
