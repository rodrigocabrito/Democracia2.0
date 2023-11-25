package pt.ul.fc.css.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.entities.Status;
import pt.ul.fc.css.entities.Votacao;

public interface VotacaoRepository extends JpaRepository<Votacao, Long> {

    /**
     * Returns a voting given its bill's id.
     * @param id the given bill's id
     * @return A voting with the given id, if it exists.
     */
    @Query("SELECT v FROM Votacao v WHERE v.projetoLei.id = :id")
    Votacao findByProjetoLeiId(@Param("id") Long id);

    /**
     * Checks if a voting with the given bill's id exists.
     * @param id the given bill's id
     * @return {@code true} if a voting with the given title exists, {@code false} otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(v) > 0 THEN true ELSE false END FROM Votacao v WHERE v.projetoLei.id = :id")
    boolean existsVotacaoById(@Param("id") Long id);


    /**
     * Returns a list of all bills, filtering with a specific status.
     * @param status the status to filter all bills
     * @return a list with all bills with the given status, if there is any.
     */
    @Query("SELECT v FROM Votacao v WHERE v.status = :status")
    List<Votacao> findByStatus(@Param("status") Status status);
}

