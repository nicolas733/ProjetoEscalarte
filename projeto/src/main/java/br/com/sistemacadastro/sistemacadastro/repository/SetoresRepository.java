package br.com.sistemacadastro.sistemacadastro.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Setores;

import java.util.List;
import java.util.Optional;

public interface SetoresRepository extends JpaRepository<Setores, Integer> {
    Setores findById(long id);
    Optional<Setores> findByNomesetor(String nomesetor);

    @Query("SELECT c FROM colaborador c WHERE c.contrato.cargos.id IN (SELECT cargo.id FROM cargoSetor cps WHERE cps.setor.id = :setorId)")
    List<Colaborador> findColaboradoresBySetorId(@Param("setorId") Integer setorId);
}
