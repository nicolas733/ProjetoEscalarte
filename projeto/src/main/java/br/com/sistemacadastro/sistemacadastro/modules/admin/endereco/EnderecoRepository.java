package br.com.sistemacadastro.sistemacadastro.modules.admin.endereco;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, String> {
    String id(int id);
}
