package pt.ul.fc.css.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.entities.Delegado;

public interface DelegadoRepository extends JpaRepository<Delegado, Long> {

    /**
     * Returns a delegate given its CC number.
     * @param nrCC given citizen's CC number
     * @return The citizen with the given CC, if it exists.
     */
	@Query("SELECT d FROM Delegado d WHERE d.nrCC = :nrCC")
    Delegado findByCC(@Param("nrCC") int nrCC);

    /**
     * Checks if a delegate with the given CC number exists.
     * @param nrCC given citizen's CC number.
     * @return {@code true} if a delegate with the given CC number exists, {@code false} otherwise.
     */
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END FROM Delegado d WHERE d.nrCC = :nrCC")
    boolean existsDelegadoByCC(@Param("nrCC") int nrCC);
}
