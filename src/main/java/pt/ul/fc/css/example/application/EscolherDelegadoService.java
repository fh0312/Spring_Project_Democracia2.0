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
import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.Tema;
import pt.ul.fc.css.example.business.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.facade.exceptions.DelegadoNotFoundException;
import pt.ul.fc.css.example.facade.exceptions.TemaNotFoundException;
@Component
public class EscolherDelegadoService {
	
	@Autowired
	private EscolherDelegadoHandler edh;
	
	public EscolherDelegadoService(EscolherDelegadoHandler edh) {
		this.edh=edh;
	}

	public void escolherDelegado(Cidadao cid,Delegado del,Tema tema) throws ApplicationException {
		if(cid==null || del==null) {
			throw new DelegadoNotFoundException("") ;
		}
		if(tema == null) {
			throw new TemaNotFoundException("");
		}
		this.edh.escolherDelegado(cid.getId(), del.getId(), tema.getId());
	}
	
	
	
}
