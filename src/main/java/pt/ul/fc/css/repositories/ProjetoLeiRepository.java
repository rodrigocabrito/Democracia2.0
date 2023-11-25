package pt.ul.fc.css.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.entities.ProjetoLei;
import pt.ul.fc.css.entities.Status;

public interface ProjetoLeiRepository extends JpaRepository<ProjetoLei, Long> {

    /**
     * Returns a list of bills given its status.
     * @param status the given status
     * @return A list of bills with the given status, if it exists any.
     */
    @Query("SELECT p FROM ProjetoLei p WHERE p.status = :status")
    List<ProjetoLei> findByStatus(@Param("status") Status status);
}