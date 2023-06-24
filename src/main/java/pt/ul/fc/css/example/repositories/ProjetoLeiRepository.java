package pt.ul.fc.css.example.repositories;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.example.business.entities.EstadoProj;
import pt.ul.fc.css.example.business.entities.ProjetoLei;

public interface ProjetoLeiRepository extends JpaRepository<ProjetoLei, Long> {
	
	@Query("SELECT pl FROM ProjetoLei pl WHERE pl.validade <= :now")
	List<ProjetoLei> findExpired(@Param("now")Timestamp now);
	
	@Query("SELECT pl FROM ProjetoLei pl WHERE pl.estado = :estado")
	List<ProjetoLei> findByStatus(@Param("estado") EstadoProj estado);

	
	@Query("SELECT pl FROM ProjetoLei pl WHERE pl.name <= :name")
	List<ProjetoLei> findByName(@Param("name")String name);
	
  

}
