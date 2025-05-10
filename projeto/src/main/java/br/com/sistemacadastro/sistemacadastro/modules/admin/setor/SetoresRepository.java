package br.com.sistemacadastro.sistemacadastro.modules.admin.setor;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SetoresRepository extends JpaRepository<Setores, String> {
    Setores findById(long id);
    Optional<Setores> findByNomesetor(String nomesetor);
}
