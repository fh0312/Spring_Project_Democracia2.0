package pt.ul.fc.css.example.business.handlers;

import java.util.List;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.DelegadoTema;
import pt.ul.fc.css.example.business.entities.Tema;
import pt.ul.fc.css.example.business.entities.TipoCidadao;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.facade.exceptions.DelegadoAlreadyExistsException;
import pt.ul.fc.css.example.facade.exceptions.DelegadoNotFoundException;
import pt.ul.fc.css.example.facade.exceptions.TemaNotFoundException;
import pt.ul.fc.css.example.repositories.CidadaoRepository;
import pt.ul.fc.css.example.repositories.DelegadoTemaRepository;
import pt.ul.fc.css.example.repositories.TemaRepository;

@Component
public class EscolherDelegadoHandler {
	
	@Autowired
	private CidadaoRepository cidadaos;
	@Autowired
	private TemaRepository temas;
	@Autowired
	private DelegadoTemaRepository delTemas;	


	public EscolherDelegadoHandler(CidadaoRepository cidadaos,TemaRepository temas, DelegadoTemaRepository delTemas) {
		
		this.cidadaos = cidadaos;
		this.temas = temas;
		this.delTemas = delTemas;
		
	}
	
	public void escolherDelegado(Long cidadao_id, Long delegado_id, Long tema_id) throws ApplicationException{
		Optional<Cidadao> delegado = cidadaos.findById(delegado_id);
		if(!delegado.isPresent()){			
			throw new DelegadoNotFoundException("Delegado não existe");
		}
		if(delegado.get().getTipoCidadao().equals(TipoCidadao.CIDADAO)){			
			throw new DelegadoNotFoundException("Cidadão não é delegado");
		}
		Delegado del = (Delegado) delegado.get();
		Optional<Tema> tema = temas.findById(tema_id);
		if(!tema.isPresent()) {
			throw new TemaNotFoundException("Tema não existe");
		}
		
		Optional<Cidadao> cidadao = cidadaos.findById(cidadao_id);
		Cidadao cd = cidadao.get();
		Tema tm = tema.get();
		
		DelegadoTema dt = delTemas.findByCidadaoTema(cd, tm);
		
		if(dt != null) {
			throw new DelegadoAlreadyExistsException("Já existe um delegado para este tema");
		}
		
		DelegadoTema dg = new DelegadoTema(tm, del, cd);
		delTemas.save(dg);
	}
	
	public List<Tema> getTemas(){
		return this.temas.findAll();
	}

	public Tema getTema(long temaId) throws TemaNotFoundException {
		Tema t = null;
		try {
			t= this.temas.findById(temaId).get();
		}
		catch(Exception e ) {
			throw new TemaNotFoundException("");
		}
		return t;
	}

	
	
	
}
