package br.com.sistemacadastro.sistemacadastro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemacadastro.sistemacadastro.cargo.Cargos;
import br.com.sistemacadastro.sistemacadastro.contrato.Contrato;

public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
    boolean existsByCargosAndAtivo(Cargos cargos, boolean ativo);

}
