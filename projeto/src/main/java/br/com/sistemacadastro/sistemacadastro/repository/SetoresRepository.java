package br.com.sistemacadastro.sistemacadastro.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemacadastro.sistemacadastro.setor.Setores;

import java.util.Optional;

public interface SetoresRepository extends JpaRepository<Setores, Integer> {
    Setores findById(long id);
    Optional<Setores> findByNomesetor(String nomesetor);
}
