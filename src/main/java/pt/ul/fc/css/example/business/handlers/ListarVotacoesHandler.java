package pt.ul.fc.css.example.business.handlers;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.EstadoProj;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;

@Component
public class ListarVotacoesHandler {
	
	@Autowired
	private ProjetoLeiRepository projetos;

	
	public ListarVotacoesHandler(ProjetoLeiRepository projetos) {		
		this.projetos = projetos;
	}
	
	public List<ProjetoLei> listarVotacoes(){
		return projetos.findByStatus(EstadoProj.ON_VOTE);		
	}
}
