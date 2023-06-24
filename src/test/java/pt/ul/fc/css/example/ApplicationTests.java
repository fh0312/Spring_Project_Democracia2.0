package pt.ul.fc.css.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.DelegadoTema;
import pt.ul.fc.css.example.business.entities.EstadoProj;
import pt.ul.fc.css.example.business.entities.EstadoVotacao;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.Tema;
import pt.ul.fc.css.example.business.entities.TipoCidadao;
import pt.ul.fc.css.example.business.entities.TipoVoto;
import pt.ul.fc.css.example.business.entities.Votacao;
import pt.ul.fc.css.example.business.entities.Voto;
import pt.ul.fc.css.example.business.handlers.ApoiarProjHandler;
import pt.ul.fc.css.example.business.handlers.FecharProjetosExpiradosHandler;
import pt.ul.fc.css.example.business.handlers.FecharVotacaoHandler;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.facade.exceptions.DelegadoNotFoundException;
import pt.ul.fc.css.example.facade.exceptions.IncorrectDateException;
import pt.ul.fc.css.example.facade.exceptions.IncorrectTitleException;
import pt.ul.fc.css.example.facade.exceptions.ProjectAlreadySupportedException;
import pt.ul.fc.css.example.facade.exceptions.TemaNotFoundException;
import pt.ul.fc.css.example.facade.exceptions.VotacaoAindaAbertaException;
import pt.ul.fc.css.example.facade.startup.DemocraciaSys;
import pt.ul.fc.css.example.repositories.CidadaoRepository;
import pt.ul.fc.css.example.repositories.DelegadoTemaRepository;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.repositories.TemaRepository;
import pt.ul.fc.css.example.repositories.VotacaoRepository;
import pt.ul.fc.css.example.repositories.VotoRepository;

@SpringBootTest
class ApplicationTests {

	@Autowired
	private ProjetoLeiRepository projetos;

	@Autowired
	private VotacaoRepository votacoes;

	@Autowired
	private VotoRepository votos;

	@Autowired
	private DelegadoTemaRepository delTemas;

	@Autowired
	private TemaRepository temas;

	@Autowired
	private CidadaoRepository cidadaos;

	@MockBean
	private DemocraciaSys app;

	@Test
	void listarVotacoes() throws FileNotFoundException, ApplicationException {
		try {
			app = new DemocraciaSys();
			app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
			app.cleanDB();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		// Base de dados limpa - nenhum projeto
		List<ProjetoLei> list = app.createListarVotacoesService().listarVotacoes();

		assertTrue(list.size() == 0);

		// populando a base de dados
		populateDb();

		Tema saude = temas.findByName("Saude").get(0);

		List<Delegado> listZes = (cidadaos.findByType(TipoCidadao.DELEGADO).stream()
				.filter(p -> p.getName().equals("ze"))).map(p -> (Delegado) p).toList();

		if (listZes.size() > 0) {
			Delegado ze = listZes.get(0);

			Timestamp dataValidade = new Timestamp(System.currentTimeMillis() + Long.parseLong("2592000000"));

			app.createApresentarProjetoService().apresentarProjeto("Projeto 1", "descricao", new File("p.txt"),
					dataValidade, saude, ze);

			List<ProjetoLei> projs = projetos.findByName("Projeto 1");
			assertTrue(projs.size() > 0);

			ProjetoLei proj1 = null;
			if (projs.size() > 0) {
				proj1 = projs.get(0);
				projetos.save(proj1);
				Voto votoZe = new Voto(ze, TipoVoto.FAVOR);
				votos.save(votoZe);

				LocalDate localDate = LocalDate.now().plus(1, ChronoUnit.MONTHS);
				LocalDateTime localDateTime = localDate.atStartOfDay();
				Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

				Votacao votacao = new Votacao(proj1, date, votoZe);

				votacoes.save(votacao);

				proj1.setVotacao(votacao);
				proj1.setStatus(EstadoProj.ON_VOTE);

				projetos.save(proj1);
			} else {
				assertFalse(true);
			}
		}

		list = app.createListarVotacoesService().listarVotacoes();

		assertTrue(list.size() > 0);

	}

	@Test
	void apresentarProj() throws ApplicationException, FileNotFoundException {
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		app.cleanDB();

		populateDb();
		projetos.deleteAll();

		Delegado ze = (Delegado) cidadaos.findByType(TipoCidadao.DELEGADO).stream()
				.filter(d -> d.getName().equals("ze")).toList().get(0);

		Tema saude = temas.findByName("Saude").get(0);
		try {
			// titulo vazio
			app.createApresentarProjetoService().apresentarProjeto("", "descricao", new File("p.txt"),
					new Timestamp(System.currentTimeMillis() + Long.parseLong("2592000000")), saude, ze);
			assertEquals(0, projetos.findAll().size());
		} catch (IncorrectTitleException title) {
			assertEquals(0, projetos.findAll().size());
		}

		try {
			// data vazia
			app.createApresentarProjetoService().apresentarProjeto("test", "descricao", new File("p.txt"), null, saude,
					ze);
			assertEquals(0, projetos.findAll().size());
		} catch (IncorrectDateException e) {
			assertEquals(0, projetos.findAll().size());
		}

		try {
			// tema vazio
			app.createApresentarProjetoService().apresentarProjeto("test", "descricao", new File("p.txt"),
					new Timestamp(System.currentTimeMillis() + Long.parseLong("2592000000")), null, ze);
			assertEquals(0, projetos.findAll().size());
		} catch (TemaNotFoundException e) {
			assertEquals(0, projetos.findAll().size());
		}

		try {
			// delegado vazio
			app.createApresentarProjetoService().apresentarProjeto("test", "descricao", new File("p.txt"),
					new Timestamp(System.currentTimeMillis() + Long.parseLong("2592000000")), saude, null);
			assertEquals(0, projetos.findAll().size());
		} catch (DelegadoNotFoundException e) {
			assertEquals(0, projetos.findAll().size());
		}

		app.createApresentarProjetoService().apresentarProjeto("Projeto 1", "descricao", new File("p.txt"),
				new Timestamp(System.currentTimeMillis() + Long.parseLong("2592000000")), saude, ze);

		ProjetoLei proj1 = projetos.findByName("Projeto 1").get(0);

		try {
			projetos.save(proj1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		assertEquals(1, projetos.findAll().size());

		Voto votoZe = new Voto(ze, TipoVoto.FAVOR);
		votos.save(votoZe);

		LocalDate localDate = LocalDate.now().plus(1, ChronoUnit.MONTHS);
		LocalDateTime localDateTime = localDate.atStartOfDay();
		Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		Votacao votacao = new Votacao(proj1, date, votoZe);

		votacoes.save(votacao);

		proj1.setVotacao(votacao);

		projetos.save(proj1);

		ProjetoLei projeto = projetos.findById(proj1.getId()).get();

		assertNotEquals(projeto.getVotacao(), null);

	}

	@Test
	void consultarProjs() throws FileNotFoundException, ApplicationException {
		app.cleanDB();
		try {
			app = new DemocraciaSys();
			app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
			app.cleanDB();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		// this method creates 4 projects
		populateDb();

		List<ProjetoLei> listProjs = app.createConsultarProjService().consultarProjetos();

		assertTrue(listProjs.size() == 4);

	}

	@Test
	void fecharProjsExpirados()
			throws NumberFormatException, FileNotFoundException, ApplicationException, InterruptedException {

		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		app.cleanDB();

		populateDb();

		projetos.deleteAll();

		Tema saude = temas.findByName("Saude").get(0);

		Delegado ze = (Delegado) cidadaos.findByName("ze").get();

		// cria projeto com validade de 1 segundo
		app.createApresentarProjetoService().apresentarProjeto("Projeto 1", "descricao", new File("p.txt"),
				new Timestamp(System.currentTimeMillis() + Long.parseLong("1000")), saude, ze);

		// fecha os projetos expirados
		new FecharProjetosExpiradosHandler(projetos).fecharProjetosExpirados();

		List<ProjetoLei> plist = projetos.findAll();

		// projeto nao pode ter sido fechado ainda não passou 1 seg.
		assertEquals(EstadoProj.ON_APPROVAL, plist.get(0).getStatus());

		ProjetoLei proj1 = projetos.findByName("Projeto 1").get(0);

		try {
			projetos.save(proj1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Voto votoZe = new Voto(ze, TipoVoto.FAVOR);
		votos.save(votoZe);

		LocalDate localDate = LocalDate.now().plus(1, ChronoUnit.MONTHS);
		LocalDateTime localDateTime = localDate.atStartOfDay();
		Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

		Votacao votacao = new Votacao(proj1, date, votoZe);

		votacoes.save(votacao);

		proj1.setVotacao(votacao);

		projetos.save(proj1);

		// Espera 1.5 seg para o projeto expirar
		Thread.sleep(1500);

		// fecha os projetos expirados
		new FecharProjetosExpiradosHandler(projetos).fecharProjetosExpirados();

		plist = projetos.findAll();

		assertTrue(plist.get(0).getStatus().equals(EstadoProj.EXPIRED));
		app.cleanDB();

	}

	@Test
	void apoiarProjeto() throws ApplicationException {

		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		app.cleanDB();

		populateDb();

		Cidadao manel = cidadaos.findByName("manel").get();
		ProjetoLei pl = projetos.findAll().get(0);
		ProjetoLei pl1 = projetos.findAll().get(1);
		long id = pl.getId();

		// primeiro apoiante
		app.createApoiarProjService().apoiarProjeto(pl, manel);
		app.createApoiarProjService().apoiarProjeto(pl1, manel);

		pl = projetos.findById(id).get();
		assertTrue(!pl.equals(null));
		assertTrue(pl.getApoiantes().size() == 2);

		Cidadao pedro = cidadaos.findByName("pedro").get();
		pl = projetos.findAll().get(0);
		id = pl.getId();

		// segundo apoiante
		app.createApoiarProjService().apoiarProjeto(pl, pedro);

		pl = projetos.findById(id).get();
		assertTrue(!pl.equals(null));
		assertTrue(pl.getApoiantes().size() == 3);

		try {
			app.createApoiarProjService().apoiarProjeto(pl, pedro);
			pl = projetos.findById(id).get();
			assertTrue(!pl.equals(null));
			assertTrue(pl.getApoiantes().size() == 3);
		} catch (ProjectAlreadySupportedException e) {
			assertTrue(pl.getApoiantes().size() == 3);
		}

	}

	@Test
	void escolherDelegado() throws ApplicationException {

		// save a few customers
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		app.cleanDB();

		populateDb();

		Cidadao manel = cidadaos.findByName("manel").get();
		Delegado ze = (Delegado) cidadaos.findByType(TipoCidadao.DELEGADO).stream().filter(d -> d.getName() == "ze")
				.toList().get(0);

		Tema t = temas.findAll().get(1);

		app.createEscolherDelegadoService().escolherDelegado(manel, ze, t);

		Delegado del_manel = delTemas.findByCidadaoTema(manel, t).getDelegado();

		assertTrue(del_manel.getId() == ze.getId());

	}

	@Test
	void votarProposta() throws ApplicationException {

		// ver lista de votacoes diponiveis
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		app.cleanDB();

		populateDb();

		List<Votacao> listV = votacoes.findAll().stream().filter(v -> v.getEstado().equals(EstadoVotacao.ABERTA))
				.toList();

		Random r = new Random();
		System.out.println("\n\n" + listV.size());

		// por cada votacao...
		for (Votacao v : listV) {
			int votosF_antes = v.getVotosFavor();
			Cidadao votante = cidadaos.findAll().get(r.nextInt(0, cidadaos.findAll().size()));
			DelegadoTema dt = delTemas.findByCidadaoTema(votante, v.getTema());
			if (dt != null) {
				Delegado del = dt.getDelegado();

				// ver voto do delegado
				Voto vdel = votos.findByVotacao(v).stream().filter(vot -> vot.getCidadao().equals(del)).toList().get(0);

				assertTrue(vdel.getCidadao().equals(del));

				app.createVotarPropostaService().votar(TipoVoto.FAVOR, v.getProj(), votante);

				Long vid = v.getId();
				v = votacoes.findById(vid).get();
				assertTrue(votosF_antes + 1 == v.getVotosFavor());

			} else {
				app.createVotarPropostaService().votar(TipoVoto.FAVOR, v.getProj(), votante);
				Long vid = v.getId();
				v = votacoes.findById(vid).get();
				assertTrue(votosF_antes + 1 == v.getVotosFavor());
			}

		}
	}

	@Test
	void fecharVotacao() throws ApplicationException, InterruptedException {
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		app.cleanDB();

		populateDb();

		FecharVotacaoHandler fvh = new FecharVotacaoHandler(projetos, cidadaos, votacoes, delTemas);
		
		List<ProjetoLei> pls = this.projetos.findAll();
		
		ProjetoLei projLei = pls.get(3);
		
		//criacao de uma nova votacao que expira dentro de 2 seg
		
		Voto voto = new Voto(projLei.getDelegado(), TipoVoto.FAVOR);
		votos.save(voto);		
		Votacao votacao = new Votacao(projLei, new Date(System.currentTimeMillis()), voto);		
		votacoes.save(votacao);
		projLei.setVotacao(votacao);
		votos.save(voto);
		projetos.save(projLei);
		
		List<Votacao> listV = votacoes.findAll().stream().filter(v -> v.getEstado().equals(EstadoVotacao.ABERTA))
				.toList();

		//esperar 2.5 seg para que a votacao expire
		
		Thread.sleep(2500);

		// por cada votacao...
		
		for (Votacao v : listV) {
			try {
				fvh.fecharVotacao(v.getId());
			} catch (VotacaoAindaAbertaException e) {
				continue;
			}
		}

		List<Votacao> listClosed = votacoes.findAll().stream().filter(v -> v.getEstado().equals(EstadoVotacao.FECHADA))
				.toList();

		assertTrue(listClosed.size() == 1);

		int open = votacoes.findAll().stream().filter(v -> v.getEstado().equals(EstadoVotacao.ABERTA)).toList().size();

		assertTrue(open == 1);
		
	}

	/**
	 * Gera e persiste alguns cidadaos aleatorios de acordo com a lista de nomes
	 * dada
	 * 
	 * @param nomes
	 * @throws ApplicationException
	 */
	private void genCidadaos(String[] nomes) throws ApplicationException {
		Random r = new Random();
		for (String nome : nomes) {
			Cidadao cid = new Cidadao(nome, nome + r.nextInt() + "@gmail.com",
					String.valueOf(r.nextInt(30000000, 40000000)), r.nextInt(910000000, 940000000));
			cidadaos.save(cid);
		}
	}

	/**
	 * Gera e persiste alguns delegados aleatorios de acordo com a lista de nomes
	 * dada
	 * 
	 * @param nomes
	 */
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

		ProjetoLei pl = this.projetos.findAll().get(0);
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

}
