package br.com.sistemacadastro.sistemacadastro.repository;

import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemacadastro.sistemacadastro.model.Solicitacoes;

public interface SolicitacaoRepository extends JpaRepository<Solicitacoes, Integer> {
    Solicitacoes findById(long id);

    void deleteByColaborador(Colaborador colaborador);
}
