package br.com.sistemacadastro.sistemacadastro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemacadastro.sistemacadastro.model.Cargos;
import br.com.sistemacadastro.sistemacadastro.model.Contrato;

import java.util.List;

public interface ContratoRepository extends JpaRepository<Contrato, Integer> {
    boolean existsByCargosAndAtivo(Cargos cargos, boolean ativo);

    List<Contrato> findByAtivoTrue();


}
