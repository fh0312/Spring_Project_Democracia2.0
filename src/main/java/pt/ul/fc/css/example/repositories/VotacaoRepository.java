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

import pt.ul.fc.css.example.business.entities.EstadoVotacao;
import pt.ul.fc.css.example.business.entities.Votacao;

public interface VotacaoRepository extends JpaRepository<Votacao, Long> {
	
	@Query("SELECT v FROM Votacao v WHERE v.estado = :estado")
	List<Votacao> findByEstado(@Param("estado") EstadoVotacao estado);

}
