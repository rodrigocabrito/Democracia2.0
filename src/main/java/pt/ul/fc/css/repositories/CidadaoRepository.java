package pt.ul.fc.css.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.entities.Cidadao;

public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {

    /**
     * Returns a citizen given its CC number.
     * @param nrCC given citizen's CC number
     * @return The citizen with the given CC, if it exists.
     */
    @Query("SELECT c FROM Cidadao c WHERE c.nrCC = :nrCC AND TYPE(c) = Cidadao")
    Cidadao findByNrCC(@Param("nrCC") int nrCC);


    /**
     * Returns the delegate CC number (as a String) given its associated theme.
     * @param partialString the String containing the theme's description
     * @return the String containing the delegate CC number, if it exists.
     */
    @Query("SELECT d FROM Cidadao c, IN (c.delegadoCCTemaDesignacao) d WHERE d LIKE %:partialString%")
    String findDelegadoCCByTemaDescription(@Param("partialString") String partialString);
}