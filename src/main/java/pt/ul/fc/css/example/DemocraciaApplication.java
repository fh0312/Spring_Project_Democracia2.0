package pt.ul.fc.css.example;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Random;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.Tema;
import pt.ul.fc.css.example.business.entities.TipoCidadao;
import pt.ul.fc.css.example.business.handlers.ApoiarProjHandler;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.facade.startup.DemocraciaSys;
import pt.ul.fc.css.example.repositories.CidadaoRepository;
import pt.ul.fc.css.example.repositories.DelegadoTemaRepository;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.repositories.TemaRepository;
import pt.ul.fc.css.example.repositories.VotacaoRepository;
import pt.ul.fc.css.example.repositories.VotoRepository;

@SpringBootApplication
public class DemocraciaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemocraciaApplication.class, args);
	}

	private ProjetoLeiRepository projetos;

	private VotacaoRepository votacoes;

	private VotoRepository votos;

	private DelegadoTemaRepository delTemas;

	private TemaRepository temas;

	private CidadaoRepository cidadaos;

	@Bean
	CommandLineRunner demo(CidadaoRepository cidadaos, ProjetoLeiRepository projetos, VotacaoRepository votacoes,
			VotoRepository votos, TemaRepository temas, DelegadoTemaRepository delTemas) {
		return (args) -> {

			this.cidadaos = cidadaos;
			this.votacoes = votacoes;
			this.projetos = projetos;
			this.votos = votos;
			this.temas = temas;
			this.delTemas = delTemas;

			// save a few customers
			DemocraciaSys app = new DemocraciaSys();
			app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
			// app.cleanDB();

			populateDb();
			Cidadao manel = cidadaos.findByName("manel").get();
			Delegado ze = (Delegado) cidadaos.findByType(TipoCidadao.DELEGADO).stream()
					.filter(d -> d.getName().equals("ze")).toList().get(0);

			Tema t = temas.findAll().get(1);

			app.createEscolherDelegadoService().escolherDelegado(manel, ze, t);


			ProjetoLei pl = this.projetos.findById((long) 1).get();
			ApoiarProjHandler aph = new ApoiarProjHandler(projetos, cidadaos, votacoes, votos);
			aph.createVotacao(pl);

			Cidadao def = new Cidadao("default", "a@a.pt", "12345", 910000000);
			Cidadao def2 = new Cidadao("default2", "a2@a.pt", "123456", 910000001);
			Delegado del = new Delegado("delDefault1", "d1@a.pt", "delegado1", 910000002);
			Delegado del2 = new Delegado("delDefault2", "d2@a.pt", "delegado2", 910000003);
			cidadaos.save(del);
			cidadaos.save(del2);
			cidadaos.save(def);
			cidadaos.save(def2);
			projetos.save(pl);

		};
	}

	private void genDelegados(String[] nomes) {
		Random r = new Random();
		for (String nome : nomes) {
			Delegado del = new Delegado(nome, nome + "_del" + r.nextInt() + "@gmail.com",
					String.valueOf(r.nextInt(30000000, 40000000)), r.nextInt(910000000, 940000000));
			cidadaos.save(del);
		}
		for (Cidadao c : cidadaos.findByType(TipoCidadao.DELEGADO)) {
			System.out.println("Inserido Delegado:" + c.getName() + c.getTel());
		}

	}

	/**
	 * Popula a base de dados com 2 Temas, 3 Cidadaos 3 Delegados e 4 projetos
	 * Random
	 * 
	 * @throws ApplicationException
	 */
	private void populateDb() throws ApplicationException {

		Tema geral = new Tema("Geral", null);
		Tema saude = new Tema("Saude", geral);

		temas.save(geral);
		temas.save(saude);

		String[] cids = { "manel", "pedro", "joao" };
		String[] dels = { "ze", "fausto", "norberto" };
		String[] projNames = { "primeiro", "segundo", "terceiro", "quarto" };

		genCidadaos(cids);
		genDelegados(dels);

		try {
			genProjetos(projNames);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ApplicationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (ProjetoLei p : projetos.findAll()) {
			System.out.println("\nProjeto: " + p.getName() + " criado por: " + p.getDelegado().getName()
					+ " com o tema: " + p.getTema().getName() + "\n");
		}
	}

	/**
	 * Create & save projects, each with the name sent in titulo parameter. All with
	 * a random Date(since 1 month and 1 day to 1 month and 10 days), Tema and
	 * Delegado.
	 * 
	 * @param titulos
	 * @throws ApplicationException
	 * @throws FileNotFoundException
	 */
	private void genProjetos(String[] titulos) throws ApplicationException, FileNotFoundException {
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		long dataValidade = System.currentTimeMillis() + Long.parseLong("2592000000");
		Random r = new Random();
		for (String t : titulos) {
			int numDels = cidadaos.findByType(TipoCidadao.DELEGADO).size();
			app.createApresentarProjetoService().apresentarProjeto(t, t + "_desc", new File("p.txt"),
					new Timestamp(dataValidade + r.nextLong(86400000, 864000000)),
					temas.findAll().get(r.nextInt(0, temas.findAll().size())),
					(Delegado) cidadaos.findByType(TipoCidadao.DELEGADO).get(r.nextInt(0, numDels)));
		}
	}

	private void genCidadaos(String[] nomes) throws ApplicationException {
		Random r = new Random();
		for (String nome : nomes) {
			Cidadao cid = new Cidadao(nome, nome + r.nextInt() + "@gmail.com",
					String.valueOf(r.nextInt(30000000, 40000000)), r.nextInt(910000000, 940000000));

			cidadaos.save(cid);
		}

	}

	@SuppressWarnings("unused")
	private void testApresentarProjeto(DemocraciaSys app) {

//    	Delegado ze = cidadaos
		// app.createApresentarProjetoService().apresentarProjeto(null, null, null,
		// null, null, null);
	}

}
