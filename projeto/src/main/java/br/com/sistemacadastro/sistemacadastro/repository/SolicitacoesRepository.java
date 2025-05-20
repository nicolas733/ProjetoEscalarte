package br.com.sistemacadastro.sistemacadastro.repository;

import br.com.sistemacadastro.sistemacadastro.model.Solicitacoes;
import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SolicitacoesRepository extends JpaRepository<Solicitacoes, Integer> {
    Solicitacoes findById(long id);

    void deleteByColaborador(Colaborador colaborador);


    @Query("""
    SELECT s FROM solicitacoes s
    JOIN s.colaborador c
    JOIN c.contrato ct
    JOIN ct.cargos cargo
    JOIN cargoSetor cps ON cps.cargo = cargo
    WHERE cps.setor.id IN (
        SELECT cps2.setor.id FROM colaborador g
        JOIN g.contrato gct
        JOIN gct.cargos gcargo
        JOIN cargoSetor cps2 ON cps2.cargo = gcargo
        WHERE g.id = :gerenteId
    )
""")
    List<Solicitacoes> findSolicitacoesPorSetorDoGerente(@Param("gerenteId") Integer gerenteId);
}
