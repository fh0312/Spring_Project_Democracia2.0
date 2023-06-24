package pt.ul.fc.css.example.business.handlers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.DelegadoTema;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.Tema;
import pt.ul.fc.css.example.business.entities.Votacao;
import pt.ul.fc.css.example.business.entities.Voto;
import pt.ul.fc.css.example.repositories.CidadaoRepository;
import pt.ul.fc.css.example.repositories.DelegadoTemaRepository;

@Component
public class CidadaoHandler {

	@Autowired
	private CidadaoRepository cidadaos;

	@Autowired
	private DelegadoTemaRepository delTemas;

	public Cidadao getFirst() {
		Optional<Cidadao> cidOp = this.cidadaos.findById((long) 0);
		if (cidOp.isPresent())
			return cidOp.get();
		return null;
	}

	public Cidadao getById(long id) {
		Optional<Cidadao> cidOp = this.cidadaos.findById(id);
		if (cidOp.isPresent())
			return cidOp.get();
		return null;
	}

	public Cidadao getByCC(String cc) {
		Optional<Cidadao> cidOp = this.cidadaos.findByCC(cc);
		if (cidOp.isPresent())
			return cidOp.get();
		return null;
	}

	public String getVotoOmissao(Cidadao cid, ProjetoLei pl) {
		try {
			Votacao votacao = pl.getVotacao();

			DelegadoTema dg = delTemas.findByCidadaoTema(cid, votacao.getTema());

			if (dg != null) {
				Voto voto = getVote(dg.getDelegado(), votacao);
				if (voto != null) {
					return voto.getTipoVoto().toString();
				} else {
					return "Não existe voto por omissao";
				}
			} else {
				List<Tema> temas = votacao.getTema().getTemasPai();
				for (Tema tema : temas) {
					DelegadoTema del = delTemas.findByCidadaoTema(cid, tema);
					if (del != null) {
						Voto voto = getVote(del.getDelegado(), votacao);
						if (voto == null) {
							return "Não existe voto por omissao";
						} else {
							return voto.getTipoVoto().toString();
						}
					}
				}
			}
		} catch (Exception e) {
			return "Não existe voto por omissao";
		}

		return "Não existe voto por omissao";
	}

	private Voto getVote(Delegado dg, Votacao votacao) {
		List<Voto> votos = votacao.getVotosDelegados();
		for (Voto v : votos) {
			if (v.getCidadao().equals(dg)) {
				return v;
			}
		}
		return null;
	}
}
