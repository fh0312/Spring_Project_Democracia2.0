package pt.ul.fc.css.example.business.handlers;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.DelegadoTema;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.Tema;
import pt.ul.fc.css.example.business.entities.TipoVoto;
import pt.ul.fc.css.example.business.entities.Votacao;
import pt.ul.fc.css.example.business.entities.Voto;
import pt.ul.fc.css.example.facade.exceptions.AlreadyVotedException;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.repositories.CidadaoRepository;
import pt.ul.fc.css.example.repositories.DelegadoTemaRepository;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.repositories.VotacaoRepository;
import pt.ul.fc.css.example.repositories.VotoRepository;

@Component
public class VotarPropostaHandler {
	
	@Autowired
	private ProjetoLeiRepository projetos;
	@Autowired
	private CidadaoRepository cidadaos;
	@Autowired
	private VotacaoRepository votacoes;
	@Autowired
	private VotoRepository votos;
	@Autowired
	private DelegadoTemaRepository delTemas;	


	public VotarPropostaHandler(ProjetoLeiRepository projetos, CidadaoRepository cidadaos, 
			VotacaoRepository votacoes, VotoRepository votos,DelegadoTemaRepository delTemas) {		
		this.projetos = projetos;
		this.cidadaos = cidadaos;
		this.votacoes = votacoes;
		this.votos = votos;
		this.delTemas = delTemas;
	}
	
	
	public List<ProjetoLei> getPropostas() {
		ListarVotacoesHandler lvh= new ListarVotacoesHandler(projetos);
		return lvh.listarVotacoes();
	}
	
	public Voto getVotoDelegado(Long projetoLei_id, Long cidadao_id) {
		Optional<ProjetoLei> pj = projetos.findById(projetoLei_id);
		ProjetoLei proj = pj.get();
		Optional<Cidadao> cd = cidadaos.findById(cidadao_id);
		Cidadao cidadao = cd.get();
		Votacao votacao = proj.getVotacao();
		
		Delegado dg = delTemas.findByCidadaoTema(cidadao, votacao.getTema()).getDelegado();
		if(dg != null) {
			Voto voto = getVote(dg, votacao);
			if(voto != null) {
				return voto;
			}
			return null;
		} else {
			List<Tema> temas = votacao.getTema().getTemasPai();
			for(Tema tema : temas) {
				Delegado del = delTemas.findByCidadaoTema(cidadao, tema).getDelegado();
				if(del != null) {
					Voto voto = getVote(del, votacao);
					if(voto != null) {					
						return voto;	
					}
				}				
			}
			return null;
		}		
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
	
	
	public void votarProposta(TipoVoto tv, Long cidadao_id, Long proj_id) throws ApplicationException{
		Optional<ProjetoLei> pj = projetos.findById(proj_id);
		ProjetoLei proj = pj.get();
		Optional<Cidadao> cd = cidadaos.findById(cidadao_id);
		Cidadao cidadao = cd.get();
		Votacao votacao = proj.getVotacao();
		if(votacao.getVotantes().contains(cidadao)) {
			throw new AlreadyVotedException("Cidadao já votou nesta proposta");
		}
		if(cidadao instanceof Delegado) {
			Voto voto = new Voto(cidadao, tv);
			Delegado deg = (Delegado) cidadao;
			votacao.votar(voto, deg);			
			votos.save(voto);
			votacoes.save(votacao);
		} else {
			votacao.votar(tv, cidadao);
			votacoes.save(votacao);
		}
		
	}
}
