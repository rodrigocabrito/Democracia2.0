package pt.ul.fc.css.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.ul.fc.css.entities.Tema;

public interface TemaRepository extends JpaRepository<Tema, Long>{

    /**
     * Returns a theme given its designation.
     * @param designacao the given bill's designation
     * @return the theme with the given designation, if it exists.
     */
    @Query("SELECT t FROM Tema t WHERE t.designacao LIKE %:designacao%")
    Tema findByDesignacao(@Param("designacao") String designacao);
}
