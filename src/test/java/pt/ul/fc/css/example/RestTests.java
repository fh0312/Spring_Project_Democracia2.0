package pt.ul.fc.css.example;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.css.example.application.ConsultarProjService;
import pt.ul.fc.css.example.application.ListarVotacoesService;
import pt.ul.fc.css.example.application.ProjetoLeiService;
import pt.ul.fc.css.example.application.DTOs.CidadaoDTO;
import pt.ul.fc.css.example.application.DTOs.ProjetoLeiDTO;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.facade.startup.DemocraciaSys;
import pt.ul.fc.css.example.repositories.CidadaoRepository;
import pt.ul.fc.css.example.repositories.DelegadoTemaRepository;
import pt.ul.fc.css.example.repositories.ProjetoLeiRepository;
import pt.ul.fc.css.example.repositories.TemaRepository;
import pt.ul.fc.css.example.repositories.VotacaoRepository;
import pt.ul.fc.css.example.repositories.VotoRepository;


public class RestTests {

	private final static String BASE_URL = "http://localhost:8080/api/";
	
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
	private ListarVotacoesService listarService;

	@MockBean
	private ProjetoLeiService projService;

	@MockBean
	private ConsultarProjService consService;

	
	void testLogin() throws IOException, ApplicationException {
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		URL loginURL = new URL(BASE_URL + "login");
		String userFail = "999999999999";
		String userSuccess = "12345";
		HttpURLConnection connectionFail = (HttpURLConnection) loginURL.openConnection();
		connectionFail.setRequestMethod("POST");
		connectionFail.setRequestProperty("RestTests", "admin");
		connectionFail.setRequestProperty("Content-Type", "application/json");
		connectionFail.setDoOutput(true);

		OutputStream osFail = connectionFail.getOutputStream();
		osFail.write(userFail.getBytes());
		osFail.flush();
		osFail.close();

		int responseCode = connectionFail.getResponseCode();
		assertEquals(HttpURLConnection.HTTP_NOT_FOUND, responseCode);

		HttpURLConnection connectionSuccess = (HttpURLConnection) loginURL.openConnection();
		connectionSuccess.setRequestMethod("POST");
		connectionSuccess.setRequestProperty("RestTests", "admin");
		connectionSuccess.setRequestProperty("Content-Type", "application/json");
		connectionSuccess.setDoOutput(true);

		OutputStream osSuccess = connectionSuccess.getOutputStream();
		osSuccess.write(userSuccess.getBytes());
		osSuccess.flush();
		osSuccess.close();

		int responseCodeSuc = connectionSuccess.getResponseCode();

		assertEquals(HttpURLConnection.HTTP_OK, responseCodeSuc);

		String response = "";
		if (responseCodeSuc == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(connectionSuccess.getInputStream()));
			String inputLine;
			StringBuffer responseAux = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseAux.append(inputLine);
			}
			in.close();
			response = responseAux.toString();
		}

		ObjectMapper om = new ObjectMapper();
		CidadaoDTO cdto = null;
		cdto = om.readValue(response, CidadaoDTO.class);
		assertEquals(userSuccess, cdto.getCc());
	}

	
	void testLogout() throws IOException {
		URL logoutURL = new URL(BASE_URL + "logout");
		HttpURLConnection connectionSuccess = (HttpURLConnection) logoutURL.openConnection();
		connectionSuccess.setRequestMethod("POST");
		connectionSuccess.setRequestProperty("RestTests", "admin");
		connectionSuccess.setRequestProperty("Content-Type", "application/json");

		int responseCode = connectionSuccess.getResponseCode();

		assertEquals(HttpURLConnection.HTTP_OK, responseCode);
	}

	 
	void testGetEmVotacao() throws IOException, ApplicationException {
		URL votesURL = new URL(BASE_URL + "votes");
		String response = "";
		List<ProjetoLeiDTO> pdtos = new ArrayList<>();
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		this.listarService = app.createListarVotacoesService();
		List<ProjetoLeiDTO> pdtosReal = this.listarService.listarVotacoesDTO();
		ObjectMapper om = new ObjectMapper();
		HttpURLConnection con = (HttpURLConnection) votesURL.openConnection();

		con.setRequestMethod("GET");
		con.setRequestProperty("RestTests", "admin");

		int responseCode = con.getResponseCode();

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer responseAux = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseAux.append(inputLine);
			}
			in.close();
			response = responseAux.toString();
			
			System.out.println("\n\n\nREPOSNSE:"+response);
			for (ProjetoLeiDTO proj : pdtosReal) {
				System.out.println("\n\n\nProj_real:"+proj.getName());
			}
			ProjetoLeiDTO[] projectArray = om.readValue(response, ProjetoLeiDTO[].class);
			
			
			for (ProjetoLeiDTO proj : projectArray) {
				pdtos.add(proj);
				System.out.println("\n\n\nProj:"+proj.getName());
			}

			for (int i = 0; i < pdtos.size(); i++) {
				assertEquals(pdtos.get(i).getName(), pdtosReal.get(i).getName());
			}
		}
		else {
			assertTrue(false);
		}
		
	}

	
	void testGetProjeto() throws Exception {
		login();
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		this.consService= app.createConsultarProjService();
		this.projService = app.createProjetoService();
		List<ProjetoLeiDTO> allProjs = this.consService.consultarProjetosDTO();

		Random r = new Random();
		String response = "";
		ObjectMapper om = new ObjectMapper();
		long randomProjId = r.nextLong(1, allProjs.size());
		URL getProjURL = new URL(BASE_URL + "projeto/" + randomProjId);

		ProjetoLeiDTO selectedProj = null;

		try {
			selectedProj = ProjetoLeiService.dtofy(projService.getById(randomProjId));
		} catch (Exception e) {
			e.printStackTrace();
		}

		ProjetoLeiDTO retrievedProj = null;

		HttpURLConnection con = (HttpURLConnection) getProjURL.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("RestTests", "admin");

		int responseCode = con.getResponseCode();
		assertEquals(HttpURLConnection.HTTP_OK, responseCode);
		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer responseAux = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseAux.append(inputLine);
			}
			in.close();
			response = responseAux.toString();
		}
		retrievedProj = om.readValue(response, ProjetoLeiDTO.class);
		assertEquals(retrievedProj.getId(), selectedProj.getId());
		con.disconnect();
		
		URL urlFail = new URL(BASE_URL + "projeto/99");
		HttpURLConnection conFail = (HttpURLConnection) urlFail.openConnection();
		conFail.setRequestMethod("GET");
		conFail.setRequestProperty("RestTests", "admin");								
		
		int plsResponseCode = conFail.getResponseCode();
		assertEquals(HttpURLConnection.HTTP_NOT_FOUND, plsResponseCode);
		conFail.disconnect();

	}

	 
	void testGetAllProjs() throws IOException, ApplicationException {
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		this.consService= app.createConsultarProjService();
		this.projService = app.createProjetoService();
		URL url = new URL(BASE_URL + "projetos");
		List<ProjetoLeiDTO> allProjs = this.consService.consultarProjetosDTO();
		String response = "";
		ObjectMapper om = new ObjectMapper();

		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("RestTests", "admin");

		int responseCode = con.getResponseCode();
		assertEquals(responseCode, HttpURLConnection.HTTP_OK);

		if (responseCode == HttpURLConnection.HTTP_OK) {
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer responseAux = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				responseAux.append(inputLine);
			}
			in.close();
			response = responseAux.toString();
		}

		ProjetoLeiDTO[] projectArray = om.readValue(response, ProjetoLeiDTO[].class);
		assertEquals(projectArray.length, allProjs.size());

	}

	 
	void testGetVotoOmissao() throws Exception {
		login();
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		this.consService= app.createConsultarProjService();
		this.projService = app.createProjetoService();
		List<ProjetoLeiDTO> allProjs = this.consService.consultarProjetosDTO();
		long idFail = 99;
		long idSucc = 1;
		URL urlFail = new URL(BASE_URL + "projeto/" + idFail + "/votar");
		URL urlSucc = new URL(BASE_URL + "projeto/" + idSucc + "/votar");
		HttpURLConnection conFail = (HttpURLConnection) urlFail.openConnection();
		conFail.setRequestMethod("GET");
		conFail.setRequestProperty("RestTests", "admin");
		int responseCodeFail = conFail.getResponseCode();
		assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, responseCodeFail);
		conFail.disconnect();

		HttpURLConnection conSucc = (HttpURLConnection) urlSucc.openConnection();
		conSucc.setRequestMethod("GET");
		conSucc.setRequestProperty("RestTests", "admin");
		int responseCodeSucc = conSucc.getResponseCode();
		assertEquals(HttpURLConnection.HTTP_OK, responseCodeSucc);
		conSucc.disconnect();
	}

	 
	void testPostApoiarProj() throws Exception {
		login();
		DemocraciaSys app = new DemocraciaSys();
		app.run(cidadaos, projetos, votacoes, votos, temas, delTemas);
		this.consService= app.createConsultarProjService();
		this.projService = app.createProjetoService();
		List<ProjetoLeiDTO> allProjs = this.consService.consultarProjetosDTO();
		Random r = new Random();
		String selectedProj = ((Long) r.nextLong(2, allProjs.size())).toString();
		String fail = ""+99;
		URL url = new URL(BASE_URL + "projeto/apoiar");
		HttpURLConnection connectionFail = (HttpURLConnection) url.openConnection();
		connectionFail.setRequestMethod("POST");
		connectionFail.setRequestProperty("RestTests", "admin");
		connectionFail.setRequestProperty("Content-Type", "application/json");
		connectionFail.setDoOutput(true);
		
		OutputStream os = connectionFail.getOutputStream();
		os.write(fail.getBytes());
		os.flush();
		os.close();
		
		int responseFail = connectionFail.getResponseCode();
		assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, responseFail);

		HttpURLConnection connectionSucc = (HttpURLConnection) url.openConnection();
		connectionSucc.setRequestMethod("POST");
		connectionSucc.setRequestProperty("RestTests", "admin");
		connectionSucc.setRequestProperty("Content-Type", "application/json");
		connectionSucc.setDoOutput(true);

		OutputStream osSucc = connectionSucc.getOutputStream();
		osSucc.write(selectedProj.getBytes());
		osSucc.flush();
		osSucc.close();

		int responseSucc = connectionSucc.getResponseCode();
		assertEquals(HttpURLConnection.HTTP_OK, responseSucc);
	}

	 
	void testPostVotar() throws Exception {
		login();
		List<ProjetoLeiDTO> allProjs = this.consService.consultarProjetosDTO();
		long idFail = allProjs.size();
		long selectedProj = 1;
		String votoFail = "blabla";
		String votoSucc = "favor";
		URL urlFail = new URL(BASE_URL + "projeto/" + idFail + "/votar");
		URL urlSucc = new URL(BASE_URL + "projeto/" + selectedProj + "/votar");
		HttpURLConnection conFail = (HttpURLConnection) urlFail.openConnection();
		conFail.setRequestMethod("POST");
		conFail.setRequestProperty("RestTests", "admin");
		conFail.setRequestProperty("Content-Type", "application/json");                   //Teste 1 - Teste com id errado
		conFail.setDoOutput(true);
		
		OutputStream osFail = conFail.getOutputStream();
		osFail.write(votoSucc.getBytes());
		osFail.flush(); 
		osFail.close();
		
		int responseCode = conFail.getResponseCode();
		assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, responseCode);                     //Fim Teste 1
		conFail.disconnect();

		HttpURLConnection conFailVote = (HttpURLConnection) urlSucc.openConnection();
		conFailVote.setRequestMethod("POST");
		conFailVote.setRequestProperty("RestTests", "admin");
		conFailVote.setRequestProperty("Content-Type", "application/json");
		conFailVote.setDoOutput(true);
		
		OutputStream osFailVote = conFailVote.getOutputStream();                          //Teste 2 - Teste com voto errado
		osFailVote.write(votoFail.getBytes());
		osFailVote.flush();
		osFailVote.close();
		
		int responseCodeFailVote = conFailVote.getResponseCode();
		assertEquals(HttpURLConnection.HTTP_BAD_REQUEST, responseCodeFailVote);           //Fim Teste 2
		conFailVote.disconnect();		
		HttpURLConnection conSucc = (HttpURLConnection) urlSucc.openConnection();
		conSucc.setRequestMethod("POST");
		conSucc.setRequestProperty("RestTests", "admin");
		conSucc.setRequestProperty("Content-Type", "application/json");
		conSucc.setDoOutput(true);

		OutputStream osSucc = conSucc.getOutputStream(); // Teste 3 - Teste com tudo correto
		osSucc.write(votoSucc.getBytes());
		osSucc.flush();
		osSucc.close();

		int responseCodeSucc = conSucc.getResponseCode();
		assertEquals(HttpURLConnection.HTTP_OK, responseCodeSucc); // Fim Teste 3

	}

	void login() throws Exception {
		URL loginURL = new URL(BASE_URL + "login");
		String userSuccess = "12345";
		HttpURLConnection connectionSuccess = (HttpURLConnection) loginURL.openConnection();
		connectionSuccess.setRequestMethod("POST");
		connectionSuccess.setRequestProperty("RestTests", "admin");
		connectionSuccess.setRequestProperty("Content-Type", "application/json");
		connectionSuccess.setDoOutput(true);

		OutputStream osSuccess = connectionSuccess.getOutputStream();
		osSuccess.write(userSuccess.getBytes());
		osSuccess.flush();
		osSuccess.close();

		int responseCodeSuc = connectionSuccess.getResponseCode();

		assertEquals(HttpURLConnection.HTTP_OK, responseCodeSuc);
	}

}
