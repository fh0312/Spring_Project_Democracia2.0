
package pt.ul.fc.css.example.repositories;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.example.business.entities.Tema;


public interface TemaRepository extends JpaRepository<Tema, Long> {
	
	@Query("SELECT t FROM Tema t WHERE t.nome = :name")
	List<Tema> findByName(@Param("name") String name);
	


	


}
