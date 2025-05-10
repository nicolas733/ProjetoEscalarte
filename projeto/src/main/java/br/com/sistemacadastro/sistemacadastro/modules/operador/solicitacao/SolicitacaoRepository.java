package br.com.sistemacadastro.sistemacadastro.modules.operador.solicitacao;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoRepository extends JpaRepository<Solicitacoes, String> {
    Solicitacoes findById(long id);
}
