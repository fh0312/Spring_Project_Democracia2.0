package pt.ul.fc.css.example.application.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pt.ul.fc.css.example.application.ApoiarProjService;
import pt.ul.fc.css.example.application.CidadaoService;
import pt.ul.fc.css.example.application.ConsultarProjService;
import pt.ul.fc.css.example.application.ListarVotacoesService;
import pt.ul.fc.css.example.application.ProjetoLeiService;
import pt.ul.fc.css.example.application.VotarPropostaService;
import pt.ul.fc.css.example.application.DTOs.ProjetoLeiDTO;
import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.TipoVoto;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;


@RestController()
@RequestMapping("api")
class Rest_Controller {

	@Autowired
	private ListarVotacoesService listarService;

	@Autowired
	private ProjetoLeiService projService;

	@Autowired
	private ConsultarProjService consService;

	@Autowired
	private CidadaoService cidService;

	@Autowired
	private ApoiarProjService apoiarService;

	private Cidadao user;

	@Autowired
	private VotarPropostaService votarService;

	@PostMapping("/login") // login
	ResponseEntity<?> login(@RequestBody String username) {
		this.user = cidService.getByCC(username);
		if (this.user == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok().body(CidadaoService.dtofy(this.user));
	}

	@PostMapping("/logout") // login
	ResponseEntity<?> logout() {
		this.user = null;
		return ResponseEntity.ok().body("");
	} 

	@GetMapping("/votes")
	List<ProjetoLeiDTO> listVotes() {
		return this.listarService.listarVotacoesDTO();
	}

	@GetMapping("/projeto/{id}")
	ResponseEntity<?> getProjetoById(@PathVariable Long id) {
		List<ProjetoLeiDTO> allProjs = this.consService.consultarProjetosDTO();
		if (id < 1 || id > allProjs.size() + 1) {
			return ResponseEntity.notFound().build();// isto é possível pois os id's são sequenciais com início no 1 logo id's
																// < 1 ou > que size + 1 são inválidos
		} else {
			ProjetoLei proj = null;
			proj = projService.getById(id);
			if (proj == null) {
				return  ResponseEntity.notFound().build();
			}

			return ResponseEntity.ok().body(ProjetoLeiService.dtofy(proj));
		}
	}

	@PostMapping("/projeto/apoiar")
	ResponseEntity<?> apoiarProjeto(@RequestBody String projetoId) {
		Long idChato = Long.parseLong(projetoId);
		List<ProjetoLeiDTO> allProjs = this.consService.consultarProjetosDTO();
		if (idChato < 1 || idChato > allProjs.size() + 1) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			ProjetoLei proj = null;
			try {
				proj = projService.getById(Long.parseLong(projetoId));
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			try {
				apoiarService.apoiarProjeto(proj, this.user);
				return ResponseEntity.ok().body(ProjetoLeiService.dtofy(proj));
			} catch (ApplicationException e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
	}

	@GetMapping("/projeto/{id}/votar")
	ResponseEntity<?> getOmissao(@PathVariable Long id) {
		List<ProjetoLeiDTO> allProjs = this.consService.consultarProjetosDTO();
		if (id < 1 || id > allProjs.size() + 1) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			ProjetoLei proj = null;
			try {
				proj = projService.getById(id);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			if (proj == null) {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			return ResponseEntity.ok().body(this.cidService.getVotoOmissao(user, proj));
		}
	}

	@PostMapping("/projeto/{id}/votar")
	ResponseEntity<?> votarProjeto(@PathVariable Long id, @RequestBody String voto) {
		List<ProjetoLeiDTO> allProjs = this.consService.consultarProjetosDTO();
		if (id < 1 || id > allProjs.size() + 1) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} else {
			ProjetoLei proj = null;
			try {
				proj = projService.getById(id);
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			if (proj == null) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
			try {
				System.out.println("\n\n\n\n\nid: " + id + " voto: " + voto + "\n\n\n\n\n");
				TipoVoto v;
				if (voto.contains("favor")) {
					v = TipoVoto.FAVOR;
				} else if (voto.contains("contra")) {
					v = TipoVoto.CONTRA;
				} else {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
				try {
					votarService.votar(v, proj, this.user);
					return ResponseEntity.ok().body(ProjetoLeiService.dtofy(proj));

				} catch (ApplicationException e) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				} catch (Exception e1) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				}
			} catch (Exception e) {
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}

	}

	@GetMapping("/projetos")
	List<ProjetoLeiDTO> getListaProjetos() {
		return consService.consultarProjetosDTO();
	}

}
