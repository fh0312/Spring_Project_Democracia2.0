package pt.ul.fc.css.example.application;

import java.util.ArrayList;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.application.DTOs.ProjetoLeiDTO;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.handlers.ConsultarProjHandler;

@Component
public class ConsultarProjService {
	
	@Autowired
	private ConsultarProjHandler cph;

	public ConsultarProjService(ConsultarProjHandler cph) {
		this.cph = cph;
	}

	public List<ProjetoLei> consultarProjetos() {
		return this.cph.listarProjs();
	}

	public List<ProjetoLeiDTO> consultarProjetosDTO() {
		List<ProjetoLeiDTO> list = new ArrayList<>();
		for(ProjetoLei pl : this.cph.listarProjs()) {
			list.add(ProjetoLeiService.dtofy(pl));
		}
		return list;
	}
	
	

}
