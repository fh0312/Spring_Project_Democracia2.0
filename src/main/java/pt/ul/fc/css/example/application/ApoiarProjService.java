package pt.ul.fc.css.example.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.handlers.ApoiarProjHandler;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
@Component
public class ApoiarProjService {
	@Autowired
	private ApoiarProjHandler aph;
	
	public ApoiarProjService(ApoiarProjHandler apH) {
		this.aph = apH;
	}
	
	public void apoiarProjeto(ProjetoLei pl, Cidadao cid) throws ApplicationException {
		this.aph.apoiarProj(pl.getId(), cid.getId());
	}

}
