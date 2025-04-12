package br.com.sistemacadastro.sistemacadastro.modules.solicitacoes.repository;

import br.com.sistemacadastro.sistemacadastro.modules.solicitacoes.model.Solicitacoes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoRepository extends JpaRepository<Solicitacoes, String> {
    Solicitacoes findById(long id);
}
