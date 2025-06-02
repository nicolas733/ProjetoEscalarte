package br.com.sistemacadastro.sistemacadastro.repository;

import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Turnos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TurnosRepository extends JpaRepository<Turnos, Integer> {

    Optional<Turnos> findByNome(String nome);
}
