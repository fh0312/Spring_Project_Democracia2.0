package pt.ul.fc.css.example.application;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.Tema;
import pt.ul.fc.css.example.business.handlers.ApresentarProjetoHandler;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;

@Component
public class ApresentarProjetoService {
	
	@Autowired
	private ApresentarProjetoHandler aph;
	
	public ApresentarProjetoService(ApresentarProjetoHandler aph) {
		this.aph = aph;
	}

	public void apresentarProjeto(String titulo, String desc, File pdf, 
			Timestamp data,Tema tema, Delegado del) 
					throws  ApplicationException {
		this.aph.proposeLawProject(titulo, desc, pdf, data, tema, del);
	}

}
