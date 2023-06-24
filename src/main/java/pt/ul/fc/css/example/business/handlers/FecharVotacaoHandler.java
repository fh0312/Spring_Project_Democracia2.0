package pt.ul.fc.css.example.business.handlers;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.DelegadoTema;
import pt.ul.fc.css.example.business.entities.EstadoProj;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.Tema;
import pt.ul.fc.css.example.business.entities.Votacao;
import pt.ul.fc.css.example.business.entities.Voto;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.facade.exceptions.VotacaoAindaAbertaException;
import pt.ul.fc.css.example.repositories.CidadaoRepository;
import pt.ul.fc.css.example.repositories.DelegadoTemaRepository;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.repositories.VotacaoRepository;

@Component
public class FecharVotacaoHandler {
	@Autowired
	private ProjetoLeiRepository projetos;
	@Autowired
	private CidadaoRepository cidadaos;
	@Autowired
	private VotacaoRepository votacoes;
	@Autowired
	private DelegadoTemaRepository delTemas;	


	public FecharVotacaoHandler(ProjetoLeiRepository projetos, CidadaoRepository cidadaos, 
			VotacaoRepository votacoes, DelegadoTemaRepository delTemas) {		
		this.projetos = projetos;
		this.cidadaos = cidadaos;
		this.votacoes = votacoes;
		this.delTemas = delTemas;
	}
	
	public void fecharVotacao(Long votacao_id) throws ApplicationException{
		
		Optional<Votacao> vt = votacoes.findById(votacao_id);
		Votacao votacao = vt.get();
		Timestamp time = new Timestamp(System.currentTimeMillis());
		Date data = time;
		if(votacao.getLimite().after(data)) {
			throw new VotacaoAindaAbertaException("Esta votação ainda não acabou");
		}
		
		List<Cidadao> cidadaos = this.cidadaos.findAll();
		List<Cidadao> lista = votacao.getVotantes();
		
		for(Cidadao c : cidadaos) {			
			if(lista.contains(c)) {
				continue;
			}			
			
			DelegadoTema dg = delTemas.findByCidadaoTema(c, votacao.getTema());
			if(dg != null) {
				
				Voto voto = getVote(dg.getDelegado(), votacao);
				if(voto != null) {					
					votacao.votar(voto.getTipoVoto(), c);					
				}
			} else {
				List<Tema> temas = votacao.getTema().getTemasPai();
				for(Tema tema : temas) {
					DelegadoTema del = delTemas.findByCidadaoTema(c, tema);
					if(del != null) {
						Voto voto = getVote(del.getDelegado(), votacao);
						if(voto != null) {					
							votacao.votar(voto.getTipoVoto(), c);	
							break;
						}
					}
				}
			}			
		}
		
		boolean tv = votacao.encerrar();
		ProjetoLei projeto = votacao.getProj();
		if(tv) {
			projeto.setStatus(EstadoProj.APPROVED);
		} else {
			projeto.setStatus(EstadoProj.DENIED);
		}
		
		
		this.votacoes.save(votacao);
		this.projetos.save(projeto);		
	}
	
	
	private Voto getVote(Delegado dg, Votacao votacao) {		
		List<Voto> votos = votacao.getVotosDelegados();
		for(Voto v : votos) {
			if(v.getCidadao().equals(dg)) {
				return v;				
			}
		}
		return null;
	}
}
