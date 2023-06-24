package pt.ul.fc.css.example.business.handlers;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.EstadoProj;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.Tema;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.facade.exceptions.DelegadoNotFoundException;
import pt.ul.fc.css.example.facade.exceptions.IncorrectDateException;
import pt.ul.fc.css.example.facade.exceptions.IncorrectDescriptionException;
import pt.ul.fc.css.example.facade.exceptions.IncorrectTitleException;
import pt.ul.fc.css.example.facade.exceptions.TemaNotFoundException;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;

@Component
public class ApresentarProjetoHandler {

	@Autowired
	private ProjetoLeiRepository projetos;
	
	
	public ApresentarProjetoHandler(ProjetoLeiRepository projetos) {		
		this.projetos = projetos;
	}
	
	public void proposeLawProject(String titulo, String descText, File anexoPDF, Timestamp data,  Tema tema, Delegado delegado)
			throws ApplicationException {
		
		if(data!=null) {
			LocalDateTime ldtPlus1 = data.toLocalDateTime().plusYears(1);
			Timestamp newTimestampPlus1 = Timestamp.valueOf(ldtPlus1);
			LocalDateTime ldtNow = LocalDateTime.now();
			Timestamp nowTimestamp = Timestamp.valueOf(ldtNow);
		
			if(titulo.length() == 0) {
				throw new IncorrectTitleException("Título inválido");
			}
			else if(descText.length() == 0) {
				throw new IncorrectDescriptionException("Descrição inválida");
			}			
			else if(data == null || newTimestampPlus1.before(data) || data.before(nowTimestamp)) {
				throw new IncorrectDateException("Data inválida");
			}
			else if(tema == null) {
				throw new TemaNotFoundException("Tema não definido");
			}
			else if (delegado == null) {
				throw new DelegadoNotFoundException("Delegado não definido");
			}
			
			
			ProjetoLei newProj = new ProjetoLei(titulo, descText, anexoPDF, data, tema, delegado);
			newProj.addApoiante(delegado);
			projetos.save(newProj);
		
		}
		
		else {
			throw new IncorrectDateException("Data não definida");
		}
	}
}
