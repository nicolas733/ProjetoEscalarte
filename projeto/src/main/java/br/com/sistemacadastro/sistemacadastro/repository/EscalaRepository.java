package br.com.sistemacadastro.sistemacadastro.repository;

import br.com.sistemacadastro.sistemacadastro.model.Colaborador;
import br.com.sistemacadastro.sistemacadastro.model.Escalas;
import br.com.sistemacadastro.sistemacadastro.model.Turnos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Repository
public interface EscalaRepository extends JpaRepository<Escalas, Integer> {

    List<Escalas> findByDataEscalaBetweenOrderByDataEscala(Date inicio, Date fim);

    /**
     * Busca todas as escalas de um colaborador numa semana (7 dias a partir da data inicial)
     */
    @Query("SELECT e FROM Escalas e WHERE e.colaborador.id = :colaboradorId " +
            "AND e.dataEscala BETWEEN :inicioSemana AND :fimSemana")
    List<Escalas> findByColaboradorAndSemana(
            @Param("colaboradorId") int colaboradorId,
            @Param("inicioSemana") Date inicioSemana,
            @Param("fimSemana") Date fimSemana);

    // Método auxiliar para facilitar a chamada do findByColaboradorAndSemana com LocalDate
    default List<Escalas> findByColaboradorAndSemana(int colaboradorId, LocalDate dataInicial) {
        Date inicio = Date.from(dataInicial.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date fim = Date.from(dataInicial.plusDays(6).atStartOfDay(ZoneId.systemDefault()).toInstant());
        return findByColaboradorAndSemana(colaboradorId, inicio, fim);
    }

    List<Escalas> findBySetoresIdAndDataEscalaBetweenOrderByDataEscala(Integer setorId, Date inicio, Date fim);
    boolean existsByColaboradorIdAndDataEscala(Long colaboradorId, Date dataEscala);


    boolean existsByTurnos(Turnos turnos);

    boolean existsByColaborador(Colaborador colaborador);
}
