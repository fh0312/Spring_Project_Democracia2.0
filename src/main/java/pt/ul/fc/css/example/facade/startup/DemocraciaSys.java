package pt.ul.fc.css.example.facade.startup;


import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Random;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */


import pt.ul.fc.css.example.application.ApoiarProjService;
import pt.ul.fc.css.example.application.ApresentarProjetoService;
import pt.ul.fc.css.example.application.ConsultarProjService;
import pt.ul.fc.css.example.application.EscolherDelegadoService;
import pt.ul.fc.css.example.application.ListarVotacoesService;
import pt.ul.fc.css.example.application.ProjetoLeiService;
import pt.ul.fc.css.example.application.VotarPropostaService;
import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.Tema;
import pt.ul.fc.css.example.business.entities.TipoCidadao;
import pt.ul.fc.css.example.business.handlers.ApoiarProjHandler;
import pt.ul.fc.css.example.business.handlers.ApresentarProjetoHandler;
import pt.ul.fc.css.example.business.handlers.ConsultarProjHandler;
import pt.ul.fc.css.example.business.handlers.EscolherDelegadoHandler;
import pt.ul.fc.css.example.business.handlers.ListarVotacoesHandler;
import pt.ul.fc.css.example.business.handlers.VotarPropostaHandler;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.repositories.CidadaoRepository;
import pt.ul.fc.css.example.repositories.DelegadoTemaRepository;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.repositories.TemaRepository;
import pt.ul.fc.css.example.repositories.VotacaoRepository;
import pt.ul.fc.css.example.repositories.VotoRepository;


public class DemocraciaSys {
	
	private CidadaoRepository cidadaos;
	private ProjetoLeiRepository projetos;
	private VotacaoRepository votacoes;
	private VotoRepository votos;
	private TemaRepository temas;
	private DelegadoTemaRepository delTemas;
	
	
	
	public void run(CidadaoRepository cidadaos, ProjetoLeiRepository projetos, VotacaoRepository votacoes,
			VotoRepository votos, TemaRepository temas,DelegadoTemaRepository delTemas) throws ApplicationException {
		this.cidadaos = cidadaos;
		this.projetos = projetos;
		this.votacoes = votacoes;
		this.votos = votos;
		this.temas = temas;
		this.delTemas = delTemas;
	}
	
	/**
	 * Closes the database connection
	 */
	public void stop() {
		
	}
	
	public ApoiarProjService createApoiarProjService() {
		return new ApoiarProjService(new ApoiarProjHandler(projetos, cidadaos, votacoes, votos));
	}
	
	public ApresentarProjetoService createApresentarProjetoService() {
		return new ApresentarProjetoService(new ApresentarProjetoHandler(projetos));
	}
	
	public ConsultarProjService createConsultarProjService() {
		return new ConsultarProjService(new ConsultarProjHandler(projetos));
	}
	
	
	
	public EscolherDelegadoService createEscolherDelegadoService() {
		return new EscolherDelegadoService(new EscolherDelegadoHandler(cidadaos, temas, delTemas));
	}
	
	public ListarVotacoesService createListarVotacoesService() {
		return new ListarVotacoesService(new ListarVotacoesHandler(projetos));
	}
	
	public VotarPropostaService createVotarPropostaService(){
		return new VotarPropostaService(new VotarPropostaHandler(projetos, cidadaos, votacoes, votos, delTemas));
	}
	
	public void cleanDB() {
		
		this.projetos.deleteAll();
		this.votacoes.deleteAll();
		this.votos.deleteAll();
		this.delTemas.deleteAll();
		this.temas.deleteAll();
		this.cidadaos.deleteAll();
		
		System.out.println("\n\nCLEANING DB\n\n");
		
	}

	public ProjetoLeiService createProjetoService(){
		return new ProjetoLeiService(new ConsultarProjHandler(projetos));
	}

	
	private void genCidadaos(String[] nomes) throws ApplicationException {
		Random r = new Random();
		for (String nome : nomes) {
			Cidadao cid = new Cidadao(nome, nome + r.nextInt() + "@gmail.com",
					String.valueOf(r.nextInt(30000000, 40000000)), r.nextInt(910000000, 940000000));
			cidadaos.save(cid);
		}
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

	
	public void populateDb() throws ApplicationException {

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
			e.printStackTrace();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
		
		List<ProjetoLei> a = this.projetos.findAll(); 
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
					+ " com o tema: " + p.getTema().getName() +"COM ID: \t"+p.getId() + "\n");
		}
	}

	
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
