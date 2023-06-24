package pt.ul.fc.di.css.javafxexample.presentation.control;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.ul.fc.di.css.javafxexample.DTOs.ProjetoLeiDTO;

public class DesktopController {

	public DesktopController() {}

	private RestAPIconnector connector = new RestAPIconnector(null);

	public String login(String user) {
		String url = "/login";
		String response = connector.post(url, user);
		return response;
	}

	public void logout() {
		String url = "/logout";
		connector.post(url, "");
	}

	public ProjetoLeiDTO votarProjeto(long id, String voto) {
		String url = "/projeto/" + id + "/votar";
		String response = connector.post(url, voto);
		ObjectMapper om = new ObjectMapper();
		ProjetoLeiDTO projeto = null;
		try {
			projeto = om.readValue(response, ProjetoLeiDTO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}catch(Exception e) {
			return projeto;
		}
		return projeto;
	}

	public ProjetoLeiDTO apoiarProj(Long id) {
		String url = "/projeto/apoiar";
		String response = connector.post(url, id.toString());
		ObjectMapper om = new ObjectMapper();
		ProjetoLeiDTO projeto = null;
		try {
			projeto = om.readValue(response, ProjetoLeiDTO.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}catch (Exception e) {
			return projeto;
		}
		return projeto;
	}

	public List<ProjetoLeiDTO> getListaProjetos() {
		String url = "/projetos";
		String response = connector.get(url);
		List<ProjetoLeiDTO> projetos = new ArrayList<>();
		ObjectMapper om = new ObjectMapper();
		try {
			ProjetoLeiDTO[] projectArray = om.readValue(response, ProjetoLeiDTO[].class);
			for (ProjetoLeiDTO project : projectArray) {
				projetos.add(project);
				
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return projetos;
	}

	public String getOmissao(long id) {
		String url = "/projeto/" + id + "/votar";
		return connector.get(url);
	}

	public ProjetoLeiDTO getProj(long id) {
		String url = "/projeto/" + id;
		String response = connector.get(url);
		ObjectMapper om = new ObjectMapper();
		ProjetoLeiDTO projeto = null;
		try {
			projeto = om.readValue(response, ProjetoLeiDTO.class);
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
		}
		return projeto;
	}

	public List<ProjetoLeiDTO> getEmVotacao() {
		String url = "/votes";
		String response = connector.get(url);
		List<ProjetoLeiDTO> projetos = new ArrayList<>();
		ObjectMapper om = new ObjectMapper();
		try {
			ProjetoLeiDTO[] projectArray = om.readValue(response, ProjetoLeiDTO[].class);
			for (ProjetoLeiDTO project : projectArray) {
				projetos.add(project);
				
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return projetos;
	}
}
