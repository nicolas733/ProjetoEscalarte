package br.com.sistemacadastro.sistemacadastro.repository;

import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GerenteRepository extends JpaRepository<Colaborador, Integer> {
    @Query("""
    select c from colaborador c
    join c.contrato ct
    join ct.cargos cargo
    join cargoSetor cps on cps.cargo = cargo
    where cps.setor.id = (
        select cps2.setor.id from colaborador g
        join g.contrato gct
        join gct.cargos gcargo
        join cargoSetor cps2 on cps2.cargo = gcargo
        where g.id = :gerenteId
    )
""")
    List<Colaborador> findColaboradoresPorSetorDoGerente(@Param("gerenteId") Integer gerenteId);
}
