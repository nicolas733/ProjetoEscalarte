package br.com.sistemacadastro.sistemacadastro.repository;

import java.util.List;
import java.util.Optional;

import br.com.sistemacadastro.sistemacadastro.model.Turnos;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ColaboradorRepository extends JpaRepository<Colaborador, Integer>{
    /*Procurando pelo id para validar o login*/
    Colaborador findById(long id);
    List<Colaborador> findAll();

   Colaborador findFirstByEmailAndSenha(String email, String senha);

    Colaborador findCollaboratorByEmail(String email);

    Optional<Colaborador> findByEmail(String email);

    @Query("SELECT DISTINCT c FROM colaborador c " +
            "JOIN c.contrato ct " +
            "JOIN ct.cargos cg " +
            "JOIN cargoSetor cps ON cps.cargo = cg " +  // join explícito via entidade
            "WHERE ct.ativo = true " +
            "AND cps.setor.id = :setorId")
    List<Colaborador> findBySetorAndContratoAtivo(@Param("setorId") int setorId);

    List<Colaborador> findByTipoUsuario(Colaborador.TipoUsuario tipoUsuario);

    List<Colaborador> findByCargoPorSetor_Setor_Id(int setorId);

    boolean existsByTurnos(Turnos turnos);

}
