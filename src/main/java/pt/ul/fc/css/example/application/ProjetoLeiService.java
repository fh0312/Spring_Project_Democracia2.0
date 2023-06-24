package pt.ul.fc.css.example.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.application.DTOs.ProjetoLeiDTO;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.handlers.ConsultarProjHandler;

@Component
public class ProjetoLeiService {

	@Autowired
	private ConsultarProjHandler cph;

	public ProjetoLeiService(ConsultarProjHandler cph) {
		this.cph = cph;
	}

	public ProjetoLei getById(Long id) {
			return this.cph.getPlById(id);
	}

	public static ProjetoLeiDTO dtofy(ProjetoLei pl) {
		ProjetoLeiDTO c2 = new ProjetoLeiDTO();
		c2.setId(pl.getId());
		c2.setName(pl.getName());
		c2.setDelegadoId(pl.getDelegado().getId());
		c2.setTemaName(pl.getTema().getName());
		c2.setTexto(pl.getTexto());
		c2.setValidade(pl.getValidade().toString());
		if(pl.getVotacao()!=null) {
			c2.setVotacaoId(pl.getVotacao().getId());
		}
		c2.setNumApoiantes(pl.getNumApoiantes());
		return c2;
	}

}
