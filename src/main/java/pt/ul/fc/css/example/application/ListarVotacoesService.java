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
import pt.ul.fc.css.example.business.handlers.ListarVotacoesHandler;

@Component
public class ListarVotacoesService {

	@Autowired
	private ListarVotacoesHandler lvh;

	public ListarVotacoesService(ListarVotacoesHandler lvh) {
		this.lvh = lvh;
	}

	public List<ProjetoLei> listarVotacoes() {
		return this.lvh.listarVotacoes();
	}
	public List<ProjetoLeiDTO> listarVotacoesDTO() {
		List<ProjetoLeiDTO> list = new ArrayList<>();
		for(ProjetoLei pl : this.lvh.listarVotacoes()) {
			list.add(ProjetoLeiService.dtofy(pl));
		}
		return list;
	}

}
