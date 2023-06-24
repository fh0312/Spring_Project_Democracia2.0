package pt.ul.fc.css.example.repositories;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.TipoCidadao;

public interface CidadaoRepository extends JpaRepository<Cidadao, Long> {
	@Query("SELECT c FROM Cidadao c WHERE c.nome = :name")
	Optional<Cidadao> findByName(@Param("name") String name);
	
	@Query("SELECT c FROM Cidadao c WHERE c.tipo = :tipo")
	List<Cidadao> findByType(@Param("tipo") TipoCidadao tipo);
	
	@Query("SELECT c FROM Cidadao c WHERE c.cc = :cc")
	Optional<Cidadao> findByCC(@Param("cc") String cc);
}
