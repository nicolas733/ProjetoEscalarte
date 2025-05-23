package br.com.sistemacadastro.sistemacadastro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemacadastro.sistemacadastro.model.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    String id(int id);
}
