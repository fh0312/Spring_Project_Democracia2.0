package pt.ul.fc.css.example.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.application.DTOs.CidadaoDTO;
import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.handlers.CidadaoHandler;

@Component
public class CidadaoService {
	
	@Autowired
	private CidadaoHandler ch;
	
	public CidadaoService(CidadaoHandler ch) {
		this.ch=ch;
	}
	
	public Cidadao getFirst() {
		return this.ch.getFirst();
	}
	
	public Cidadao getById(long id) {
		return this.ch.getById(id);
	}

	public Cidadao getByCC(String cc) {
		return this.ch.getByCC(cc);
	}
	
	public String getVotoOmissao(Cidadao cid,ProjetoLei proj) {
		return this.ch.getVotoOmissao(cid,proj);
		
	}
	
	public static CidadaoDTO dtofy(Cidadao pl) {
		CidadaoDTO c2 = new CidadaoDTO();
		c2.setId(pl.getId());
		c2.setName(pl.getName());
		c2.setCc(pl.getCc());
		c2.setMail(pl.getMail());
		c2.setTel(pl.getTel());
		c2.setDelegado(pl.isDelegado());
		c2.setTipoCidadao(pl.getTipoCidadao().name());
		return c2;
	}
}
