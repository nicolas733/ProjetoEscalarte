package br.com.sistemacadastro.sistemacadastro.modules.admin.repositorys;

import br.com.sistemacadastro.sistemacadastro.modules.admin.Entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {
}
