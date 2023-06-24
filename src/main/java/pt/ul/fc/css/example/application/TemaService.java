package pt.ul.fc.css.example.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.Tema;
import pt.ul.fc.css.example.business.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.example.facade.exceptions.TemaNotFoundException;

@Component
public class TemaService {

	@Autowired
	private EscolherDelegadoHandler edh;

	public TemaService(EscolherDelegadoHandler edh) {
		this.edh = edh;
	}

	public List<Tema> getById() {
		return this.edh.getTemas();
	}

	public List<Tema> getTemas() {
		return this.edh.getTemas();
	}

	public Tema getTema(String temaId) throws NumberFormatException, TemaNotFoundException {
		return this.edh.getTema(Long.parseLong(temaId));
		
	}

}
