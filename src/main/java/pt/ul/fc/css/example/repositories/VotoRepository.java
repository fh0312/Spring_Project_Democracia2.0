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

import pt.ul.fc.css.example.business.entities.Votacao;
import pt.ul.fc.css.example.business.entities.Voto;

public interface VotoRepository extends JpaRepository<Voto, Long> {
	
	@Query("SELECT v FROM Voto v WHERE v.votacao = :vot")
	List<Voto> findByVotacao(@Param("vot")Votacao vot);

}
