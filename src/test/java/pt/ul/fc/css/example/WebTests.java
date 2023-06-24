package pt.ul.fc.css.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import pt.ul.fc.css.example.application.ListarVotacoesService;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.TipoVoto;
import pt.ul.fc.css.example.facade.startup.DemocraciaSys;
import pt.ul.fc.css.example.repositories.CidadaoRepository;
import pt.ul.fc.css.example.repositories.DelegadoTemaRepository;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.repositories.TemaRepository;
import pt.ul.fc.css.example.repositories.VotacaoRepository;
import pt.ul.fc.css.example.repositories.VotoRepository;

@SpringBootTest
@AutoConfigureMockMvc
class WebTests {

	@Autowired
	private MockMvc mockMvc;

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

	@MockBean
	private ListarVotacoesService listarService;

	// Tests

	@Test
	void login() throws Exception {

		// caso normal
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(MockMvcResultMatchers.view().name("index"));

		this.mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "delegado1"));

		this.mockMvc.perform(get("/")).andDo(print()).andExpect(MockMvcResultMatchers.model().attributeExists("user"))
				.andDo(MockMvcResultHandlers.print());

		this.mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "unknown"));

		this.mockMvc.perform(get("/")).andDo(print())
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("user"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	void listarVotacoesEmCurso() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "delegado1"));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/votes")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("votes", listarService.listarVotacoes()))
				.andExpect(MockMvcResultMatchers.view().name("votes_list")).andDo(MockMvcResultHandlers.print());
	}

	@Test
	void apresentarProj() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "delegado1"));

		// caso normal
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/apresentarproj")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content("temaId=1&title=test1&descText=descTest&date=2023-10-10"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("apresentar_proj"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "Projeto apresentado com sucesso!"))
				.andDo(MockMvcResultHandlers.print());

		ProjetoLei pl = this.projetos.findByName("test1").get(0);
		assertNotNull(pl);

		// excessoes

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/apresentarproj")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content("temaId=1&title=test1&descText=descTest&date=2020-10-10"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("apresentar_proj"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "ERRO: Data incorreta!"))
				.andDo(MockMvcResultHandlers.print());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/apresentarproj")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content("temaId=99&title=test1&descText=descTest&date=2023-10-10"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("apresentar_proj"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "ERRO: Tema incorreto!"))
				.andDo(MockMvcResultHandlers.print());

		this.mockMvc.perform(MockMvcRequestBuilders.post("/logout"))
				.andExpect(MockMvcResultMatchers.redirectedUrl("/"));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/apresentarproj")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content("temaId=1&title=test1&descText=descTest&date=2023-10-10"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("apresentar_proj"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "ERRO: Delegado não encontrado!"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	void consultarProjs() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "delegado1"));
		this.mockMvc.perform(MockMvcRequestBuilders.get("/projeto")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("projetos_list")).andDo(MockMvcResultHandlers.print())
				.andExpect(MockMvcResultMatchers.model().attributeExists("projetos"));
	}

	@Test
	void apoiarProj() throws Exception {
		// login
		this.mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "delegado1"));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/projeto/2"));

		// Delegado apoia projetos

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/projeto/apoiar")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("projetoId=2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("projeto_detail"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "Projeto apoiado com sucesso!"))
				.andDo(MockMvcResultHandlers.print());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/projeto/apoiar")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("projetoId=2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("projeto_detail"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "Projeto já apoiado!"))
				.andDo(MockMvcResultHandlers.print());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/projeto/3"));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/projeto/apoiar")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("projetoId=3"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("projeto_detail"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "Projeto apoiado com sucesso!"))
				.andDo(MockMvcResultHandlers.print());

		// Cidadao apoia projetos

		this.mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "12345"));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/projeto/apoiar")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("projetoId=2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("projeto_detail"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "Projeto apoiado com sucesso!"))
				.andDo(MockMvcResultHandlers.print());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/projeto/apoiar")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("projetoId=2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("projeto_detail"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "Projeto já apoiado!"))
				.andDo(MockMvcResultHandlers.print());

		this.mockMvc.perform(MockMvcRequestBuilders.get("/projeto/3"));

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/projeto/apoiar")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("projetoId=3"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("projeto_detail"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "Projeto apoiado com sucesso!"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	void votarProj() throws Exception {
		// login
		this.mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "delegado1"));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/projeto/1"));

		// votar proj

		TipoVoto tv = TipoVoto.FAVOR;
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/projeto/votar")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("projetoId=1&voto=" + tv))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("projeto_detail")).andDo(MockMvcResultHandlers.print());

		tv = TipoVoto.CONTRA;
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/projeto/votar")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("projetoId=1&voto=" + tv))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("projeto_detail"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "Já votou neste projeto!"))
				.andDo(MockMvcResultHandlers.print());

	}

	@Test
	void escolherDel() throws Exception {
		// Cidadao escolhe delegado
		this.mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "123456"));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/escolherDel"));

		// escolher certo

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/escolher/del").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content("temaId=1&delCC=delegado1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem",
						"Delegado: delegado1 escolhido com sucesso!"))
				.andExpect(MockMvcResultMatchers.view().name("escolher_del")).andDo(MockMvcResultHandlers.print());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/escolher/del")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("temaId=1&delCC=delegado99"))
				.andExpect(MockMvcResultMatchers.view().name("escolher_del"))
				.andExpect(
						MockMvcResultMatchers.model().attribute("mensagem", "ERRO: O delegado introduzido não existe!"))
				.andDo(MockMvcResultHandlers.print());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/escolher/del")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("temaId=99&delCC=delegado1"))
				.andExpect(MockMvcResultMatchers.view().name("escolher_del"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "ERRO: Esse tema não existe!"))
				.andDo(MockMvcResultHandlers.print());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/escolher/del")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("temaId=1&delCC=delegado1"))
				.andExpect(MockMvcResultMatchers.view().name("escolher_del")).andExpect(MockMvcResultMatchers.model()
						.attribute("mensagem", "ERRO: Já escolheu um delegado para este tema!"))
				.andDo(MockMvcResultHandlers.print());

		// Delegado escolhe delegado
		this.mockMvc.perform(MockMvcRequestBuilders.post("/login").param("username", "delegado2"));

		this.mockMvc.perform(MockMvcRequestBuilders.get("/escolherDel"));

		// escolher certo

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/escolher/del").contentType(MediaType.APPLICATION_FORM_URLENCODED)
						.content("temaId=1&delCC=delegado1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem",
						"Delegado: delegado1 escolhido com sucesso!"))
				.andExpect(MockMvcResultMatchers.view().name("escolher_del")).andDo(MockMvcResultHandlers.print());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/escolher/del")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("temaId=1&delCC=delegado99"))
				.andExpect(MockMvcResultMatchers.view().name("escolher_del"))
				.andExpect(
						MockMvcResultMatchers.model().attribute("mensagem", "ERRO: O delegado introduzido não existe!"))
				.andDo(MockMvcResultHandlers.print());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/escolher/del")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("temaId=99&delCC=delegado1"))
				.andExpect(MockMvcResultMatchers.view().name("escolher_del"))
				.andExpect(MockMvcResultMatchers.model().attribute("mensagem", "ERRO: Esse tema não existe!"))
				.andDo(MockMvcResultHandlers.print());

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/escolher/del")
						.contentType(MediaType.APPLICATION_FORM_URLENCODED).content("temaId=1&delCC=delegado1"))
				.andExpect(MockMvcResultMatchers.view().name("escolher_del")).andExpect(MockMvcResultMatchers.model()
						.attribute("mensagem", "ERRO: Já escolheu um delegado para este tema!"))
				.andDo(MockMvcResultHandlers.print());
	}
}
