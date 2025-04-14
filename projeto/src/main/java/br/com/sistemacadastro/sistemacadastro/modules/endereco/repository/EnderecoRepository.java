package br.com.sistemacadastro.sistemacadastro.modules.endereco.repository;

import br.com.sistemacadastro.sistemacadastro.modules.endereco.model.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {
}
