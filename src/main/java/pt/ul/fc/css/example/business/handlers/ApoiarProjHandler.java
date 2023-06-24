package pt.ul.fc.css.example.business.handlers;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.EstadoProj;
import pt.ul.fc.css.example.business.entities.EstadoVotacao;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.TipoVoto;
import pt.ul.fc.css.example.business.entities.Votacao;
import pt.ul.fc.css.example.business.entities.Voto;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.facade.exceptions.CidadaoNotFoundException;
import pt.ul.fc.css.example.facade.exceptions.ProjectAlreadySupportedException;
import pt.ul.fc.css.example.facade.exceptions.ProjectNotFoundException;
import pt.ul.fc.css.example.repositories.CidadaoRepository;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.repositories.VotacaoRepository;
import pt.ul.fc.css.example.repositories.VotoRepository;

@Component
public class ApoiarProjHandler {
	
	
	@Autowired
	private ProjetoLeiRepository projetos;
	@Autowired
	private CidadaoRepository cidadaos;
	@Autowired
	private VotacaoRepository votacoes;
	@Autowired
	private VotoRepository votos;	


	public ApoiarProjHandler(ProjetoLeiRepository projetos, CidadaoRepository cidadaos, 
			VotacaoRepository votacoes, VotoRepository votos) {		
		this.projetos = projetos;
		this.cidadaos = cidadaos;
		this.votacoes = votacoes;
		this.votos = votos;
	}
	
	public void apoiarProj(Long proj_id, Long cidadao_id) throws ApplicationException{
		
        
		Optional<ProjetoLei> proj = projetos.findById(proj_id);	
		if(!proj.isPresent()) {
			throw new ProjectNotFoundException("Projeto não existe");
		}	
		ProjetoLei projLei = proj.get();
		List<Cidadao> apoiantes = projLei.getApoiantes();
		Optional<Cidadao> cid_aux = cidadaos.findById(cidadao_id);
		
		if(!cid_aux.isPresent()) {
			throw new CidadaoNotFoundException("Cidadão não existe");
		}
		Cidadao cidadao = cid_aux.get();
		
		
		for(Cidadao c : apoiantes) {
			if (c.getId() == cidadao.getId()) {
				throw new ProjectAlreadySupportedException("Cidadão já apoiou este projeto");
			}
		}
		
		
		
		
		projLei.addApoiante(cidadao);
		
		projetos.save(projLei);
	
		if(projLei.getNumApoiantes() >= 10000) {
			createVotacao(projLei);
		}
	}
	
	
	public void createVotacao(ProjetoLei projLei) {		 
		Timestamp tproj = projLei.getValidade();
		
		projLei.setStatus(EstadoProj.ON_VOTE);
		projetos.save(projLei);
		
		Timestamp now = new Timestamp(System.currentTimeMillis());
		
		LocalDateTime days15 = now.toLocalDateTime().plusDays(15);
		
		LocalDateTime months2 = now.toLocalDateTime().plusMonths(2);
		
		Timestamp voteTime;
		
		if(tproj.before(Timestamp.valueOf(days15))) {
			voteTime = Timestamp.valueOf(days15);
		} 
		else if(tproj.after(Timestamp.valueOf(months2))) {
			voteTime = Timestamp.valueOf(months2);
		} else {
			voteTime = tproj;
		}
		Date datavote = voteTime;
		
		Voto voto = new Voto(projLei.getDelegado(), TipoVoto.FAVOR);
		
		votos.save(voto);		
		Votacao votacao = new Votacao(projLei, datavote, voto);		
		votacoes.save(votacao);
		projLei.setVotacao(votacao);
		//voto.setVotacao(votacao);
		votos.save(voto);
		projetos.save(projLei);		

	}
}
