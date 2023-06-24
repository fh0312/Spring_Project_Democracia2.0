package pt.ul.fc.css.example.business.handlers;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.EstadoProj;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;

@Component
public class FecharProjetosExpiradosHandler {
	
	@Autowired
	private ProjetoLeiRepository projetos;	


	public FecharProjetosExpiradosHandler(ProjetoLeiRepository projetos) {		
		this.projetos = projetos;
	}
	
	public void fecharProjetosExpirados() {
		
        List<ProjetoLei> projetosExpirados = projetos.findExpired(new Timestamp(System.currentTimeMillis()));
        for (ProjetoLei projeto : projetosExpirados) {
        	
        	if(! projeto.getStatus().equals(EstadoProj.EXPIRED)) {
        		if(projeto.getStatus().equals(EstadoProj.ON_APPROVAL)) {
        			projeto.setStatus(EstadoProj.EXPIRED);
        			projetos.save(projeto);
        		}
        	}       	
        }
    }
	
	
	
	 
	
}
