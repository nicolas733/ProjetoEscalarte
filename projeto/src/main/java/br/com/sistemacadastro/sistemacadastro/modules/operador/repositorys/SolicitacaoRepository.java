package br.com.sistemacadastro.sistemacadastro.modules.operador.repositorys;

import br.com.sistemacadastro.sistemacadastro.modules.operador.Entitys.Solicitacoes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoRepository extends JpaRepository<Solicitacoes, String> {
    Solicitacoes findById(long id);
}
