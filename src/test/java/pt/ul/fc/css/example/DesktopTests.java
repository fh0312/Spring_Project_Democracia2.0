package pt.ul.fc.css.example;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.css.example.application.ConsultarProjService;
import pt.ul.fc.css.example.application.ListarVotacoesService;
import pt.ul.fc.css.example.application.ProjetoLeiService;
import pt.ul.fc.css.example.application.DTOs.ProjetoLeiDTO;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.TipoVoto;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;

@SpringBootTest
@AutoConfigureMockMvc
public class DesktopTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProjetoLeiRepository projetos;

	@Autowired
	private ListarVotacoesService listarService;

	@Autowired
	private ConsultarProjService consultarService;


	private static ObjectMapper mapper = new ObjectMapper();

	@Test
	void login() throws Exception {
		// caso normal
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/login").content("12345")).andExpect(status().isOk())
				.andExpect(jsonPath("$.cc").value("12345"));

		// excessao
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/login").content("unknown"))
				.andExpect(status().isNotFound());

	}

	@Test
	void logout() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/logout")).andExpect(status().isOk());

	}

	@Test
	void listarVotacoesEmCurso() throws Exception {

		List<ProjetoLeiDTO> pldto = new ArrayList<>();
		for (ProjetoLei pl : listarService.listarVotacoes()) {
			pldto.add(ProjetoLeiService.dtofy(pl));
		}

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/login").param("username", "delegado1"));

		String response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/votes"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

		ProjetoLeiDTO[] plret = mapper.readValue(response, ProjetoLeiDTO[].class);
		Assertions.assertEquals(plret.length, pldto.size());
	}

	@Test
	void consultarProjs() throws Exception {

		List<ProjetoLeiDTO> pldto = new ArrayList<>();

		for (ProjetoLei pl : this.projetos.findAll()) {
			pldto.add(ProjetoLeiService.dtofy(pl));
		}

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/login").param("username", "delegado1"));
		String response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/projetos"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

		ProjetoLeiDTO[] plret = mapper.readValue(response, ProjetoLeiDTO[].class);

		Assertions.assertEquals(plret.length, pldto.size());

	}

	@Test
	void apoiarProj() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/login").content("12345")).andExpect(status().isOk());

		ProjetoLeiDTO pldto = ProjetoLeiService.dtofy(this.consultarService.consultarProjetos().get(0));

		String id = "" + pldto.getId();


		String response = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/api/projeto/apoiar").content("" + pldto.getId()))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		Assertions.assertEquals(pldto.getName(), mapper.readValue(response, ProjetoLeiDTO.class).getName());

		// nao existe proj
		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/projeto/apoiar").content("" + 99))
				.andExpect(status().isBadRequest());
	}

	@Test
	void votarProj() throws Exception {

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/login").content("123456")).andExpect(status().isOk());

		ProjetoLeiDTO pldto = ProjetoLeiService.dtofy(this.listarService.listarVotacoes().get(0));


		String id = "" + pldto.getId();


		String response = this.mockMvc
				.perform(MockMvcRequestBuilders.post("/api/projeto/" + id + "/votar").content("favor"))
				.andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

		// nao existe proj

		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/api/projeto/" + 99 + "/votar").content(TipoVoto.FAVOR.name()))
				.andExpect(status().isBadRequest());

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/projeto/" + 99 + "/votar").content("unknown"))
				.andExpect(status().isBadRequest());

	}

	@Test
	void getVotoOmissao() throws Exception {
		ProjetoLeiDTO pldto = null;

		for (ProjetoLei pl : listarService.listarVotacoes()) {
			pldto = (ProjetoLeiService.dtofy(pl));
			break;
		}

		assertNotNull(pldto);

		this.mockMvc.perform(MockMvcRequestBuilders.post("/api/login").content("12345")).andExpect(status().isOk());

		String response = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/projeto/" + pldto.getId() + "/votar"))
				.andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();

		this.mockMvc.perform(MockMvcRequestBuilders.get("/api/projeto/" + 99 + "/votar"))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}

}
