package pt.ul.fc.css.example.business.handlers;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.EstadoProj;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;

@Component
public class ConsultarProjHandler {
	
	@Autowired
	private ProjetoLeiRepository projetos;	


	public ConsultarProjHandler(ProjetoLeiRepository projetos) {		
		this.projetos = projetos;
	}
	
	public List<ProjetoLei> listarProjOnApproval(){      
		return projetos.findByStatus(EstadoProj.ON_APPROVAL);	
	}
	
	public List<ProjetoLei> listarProjs(){    
		List<ProjetoLei> listpl = listarProjOnApproval();
		listpl.addAll(projetos.findByStatus(EstadoProj.ON_VOTE));
		return listpl;	
	}
	
	public ProjetoLei getPlById(Long id) {
			Optional<ProjetoLei> oPl = this.projetos.findById(id);
			if(oPl.isPresent()) {
				return oPl.get();
			}
			return null;
	}
}
