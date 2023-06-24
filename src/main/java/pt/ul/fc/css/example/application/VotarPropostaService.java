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
import pt.ul.fc.css.example.business.entities.TipoVoto;
import pt.ul.fc.css.example.business.handlers.VotarPropostaHandler;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
@Component
public class VotarPropostaService {
	
	@Autowired
	private VotarPropostaHandler vph ;
	
	public VotarPropostaService(VotarPropostaHandler votarPh) {
		this.vph = votarPh;
	}
	
	public void votar(TipoVoto tv, ProjetoLei pl , Cidadao cid) throws ApplicationException {
		this.vph.votarProposta(tv,cid.getId(), pl.getId());
	}
}
