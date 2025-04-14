package br.com.sistemacadastro.sistemacadastro.modules.contrato.repository;

import br.com.sistemacadastro.sistemacadastro.modules.contrato.model.Contrato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContratoRepository extends JpaRepository<Contrato, String> {
}
