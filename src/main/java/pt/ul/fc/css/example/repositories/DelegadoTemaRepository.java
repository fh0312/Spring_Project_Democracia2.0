package pt.ul.fc.css.example.repositories;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.DelegadoTema;
import pt.ul.fc.css.example.business.entities.Tema;

public interface DelegadoTemaRepository extends JpaRepository<DelegadoTema, Long> {
	
		@Query("SELECT dt FROM DelegadoTema dt WHERE dt.cidadao = :cid AND dt.tema = :tema")
		DelegadoTema findByCidadaoTema(@Param("cid")Cidadao cidadao,@Param("tema") Tema tema);
}
