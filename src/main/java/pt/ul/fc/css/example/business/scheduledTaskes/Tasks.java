package pt.ul.fc.css.example.business.scheduledTaskes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.Votacao;
import pt.ul.fc.css.example.business.handlers.FecharProjetosExpiradosHandler;
import pt.ul.fc.css.example.business.handlers.FecharVotacaoHandler;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.repositories.VotacaoRepository;

@Component
public class Tasks {

	@Autowired
	private FecharVotacaoHandler fvh;

	@Autowired
	private VotacaoRepository votacoes;

	@Autowired
	private FecharProjetosExpiradosHandler fpeh;

	@Scheduled(fixedDelay = 60000) // Executa a cada 60 segundos
	public void FecharVotacoes() {
		for (Votacao votacao : this.votacoes.findAll()) {
			try {
				this.fvh.fecharVotacao(votacao.getId());
			} catch (ApplicationException e) {
				continue;
			}
		}
	}

	@Scheduled(fixedDelay = 60000) // Executa a cada 60 segundos
	public void FecharProjetos() {
		this.fpeh.fecharProjetosExpirados();
	}

}
