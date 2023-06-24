package pt.ul.fc.css.example.application.controllers;

import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import pt.ul.fc.css.example.application.ApoiarProjService;
import pt.ul.fc.css.example.application.ApresentarProjetoService;
import pt.ul.fc.css.example.application.CidadaoService;
import pt.ul.fc.css.example.application.ConsultarProjService;
import pt.ul.fc.css.example.application.EscolherDelegadoService;
import pt.ul.fc.css.example.application.ListarVotacoesService;
import pt.ul.fc.css.example.application.ProjetoLeiService;
import pt.ul.fc.css.example.application.TemaService;
import pt.ul.fc.css.example.application.VotarPropostaService;
import pt.ul.fc.css.example.business.entities.Cidadao;
import pt.ul.fc.css.example.business.entities.Delegado;
import pt.ul.fc.css.example.business.entities.EstadoProj;
import pt.ul.fc.css.example.business.entities.ProjetoLei;
import pt.ul.fc.css.example.business.entities.TipoVoto;
import pt.ul.fc.css.example.facade.exceptions.ApplicationException;
import pt.ul.fc.css.example.facade.exceptions.DelegadoAlreadyExistsException;
import pt.ul.fc.css.example.facade.exceptions.DelegadoNotFoundException;
import pt.ul.fc.css.example.facade.exceptions.IncorrectDateException;
import pt.ul.fc.css.example.facade.exceptions.IncorrectDescriptionException;
import pt.ul.fc.css.example.facade.exceptions.IncorrectTitleException;
import pt.ul.fc.css.example.facade.exceptions.TemaNotFoundException;

@Controller
public class WebController {

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

	@Autowired
	private EscolherDelegadoService escolherDelService;

	@Autowired
	private TemaService temaService;

	@Autowired
	private ApresentarProjetoService aps;

	public WebController() {
		super(); 
	}

	@GetMapping("/") // HOME
	public String getIndex(Model model) {
		model.addAttribute("user", this.user);
		return "index";
	}

	@PostMapping("/login") // login
	public String login(@RequestParam("username") String username, Model model) {
		this.user = cidService.getByCC(username);
		model.addAttribute("user", this.user);		
		return "redirect:/";
	}

	@PostMapping("/logout") // login
	public String logout(Model model) {
		this.user = null;
		return "redirect:/";
	}

	@GetMapping("/escolherDel") // HOME
	public String escolherDelegado(Model model) {
		model.addAttribute("user", this.user);
		model.addAttribute("temas", this.temaService.getTemas());
		return "escolher_del";
	}

	@PostMapping("/escolher/del")
	public String escolherDel(@RequestParam("temaId") String temaId, @RequestParam("delCC") String delCC, Model model) {
		model.addAttribute("user", this.user);
		model.addAttribute("temas", this.temaService.getTemas());
		String mensagem = "Delegado: " + delCC + " escolhido com sucesso!";
		try {
			this.escolherDelService.escolherDelegado(this.user, (Delegado) this.cidService.getByCC(delCC),
					this.temaService.getTema(temaId));
		} catch (TemaNotFoundException temaE) {
			mensagem = "ERRO: Esse tema não existe!";
		} catch (DelegadoNotFoundException delE) {
			mensagem = "ERRO: O delegado introduzido não existe!";
		} catch (DelegadoAlreadyExistsException delE2) {
			mensagem = "ERRO: Já escolheu um delegado para este tema!";
		} catch (ApplicationException e) {
			mensagem = "ERRO !";
		}
		model.addAttribute("mensagem", mensagem);

		return "escolher_del";
	}

	@GetMapping("/apresentar") // HOME
	public String apresentar(Model model) {
		model.addAttribute("user", this.user);
		model.addAttribute("temas", this.temaService.getTemas());
		return "apresentar_proj";
	}

	@PostMapping("/apresentarproj")
	public String apresentarProj(Model model, @RequestParam("temaId") String temaId,
			@RequestParam("title") String titulo, @RequestParam("descText") String desc,
			@RequestParam("date") String data) {
		model.addAttribute("temas", this.temaService.getTemas());
		
		Delegado user = (Delegado) this.user;
		Timestamp ts = Timestamp.valueOf(data + " 00:00:00");
		String mensagem = "";
		System.out.println("\n\n\n\n\n\n\n"+temaId+"\n\n\n\n\n\n\n\n");
		
		try {
			aps.apresentarProjeto(titulo, desc, null, ts, this.temaService.getTema(temaId), user);
			System.out.println("\n\n\n\n\n\n\n"+this.user.getName()+"\n\n\n\n\n\n\n\n");
			mensagem = "Projeto apresentado com sucesso!";
			
		} catch (IncorrectTitleException e) {
			mensagem = "ERRO: Titulo incorreto!";
		} catch (IncorrectDescriptionException e) {
			mensagem = "ERRO: Descrição incorreta!";
		} catch (IncorrectDateException e) {
			mensagem = "ERRO: Data incorreta!";
		} catch (TemaNotFoundException e) {
			mensagem = "ERRO: Tema incorreto!";
		} catch (DelegadoNotFoundException e) {
			mensagem = "ERRO: Delegado não encontrado!";
		} catch (ApplicationException e) {
			mensagem = "ERRO!";
		}
		model.addAttribute("mensagem", mensagem);
		return "apresentar_proj";
	}

	@GetMapping("/votes")
	public String listVotes(final Model model) {
		model.addAttribute("votes", listarService.listarVotacoes());
		return "votes_list";
	}

	@GetMapping("/projeto/{id}")
	public String getProjetoById(@PathVariable("id") Long id, Model model) {
		model.addAttribute("apoiarService", apoiarService);
		ProjetoLei proj = null;
		try {
			proj = projService.getById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(proj==null) {
			return "projeto_detail";
		}
		model.addAttribute("projeto", proj);
		model.addAttribute("mensagem", "");
		model.addAttribute("favor", TipoVoto.FAVOR);
		model.addAttribute("contra", TipoVoto.CONTRA);
		
		if(proj.getStatus().equals(EstadoProj.ON_VOTE)) {
			model.addAttribute("votoOmissao", this.cidService.getVotoOmissao(user, proj));			
		}

		return "projeto_detail";
	}

	@PostMapping("/projeto/apoiar")
	public String apoiarProjeto(@RequestParam("projetoId") String projetoId, Model model) {
		String mensagem = "Projeto apoiado com sucesso!";
		model.addAttribute("apoiarService", apoiarService);		
		ProjetoLei proj = null;
		try {
			proj = projService.getById(Long.parseLong(projetoId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(proj.getStatus().equals(EstadoProj.ON_VOTE)) {
			model.addAttribute("votoOmissao", this.cidService.getVotoOmissao(user, proj));
		}
		model.addAttribute("projeto", proj);
		try {			
			apoiarService.apoiarProjeto(proj, this.user);
			model.addAttribute("mensagem", mensagem);
			
		} catch (ApplicationException e) {
			mensagem = "Projeto já apoiado!";			
			model.addAttribute("mensagem", mensagem);
		}
		return "projeto_detail";
	}

	@PostMapping("/projeto/votar")
	public String votarProjeto(@RequestParam("projetoId") String projetoId, @RequestParam("voto") TipoVoto voto,
			Model model) {
		ProjetoLei proj=null;
		try {
			proj = projService.getById(Long.parseLong(projetoId));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		String mensagem = "Voto no projeto: " + proj.getName() + " processado com sucesso!";
		model.addAttribute("votoOmissao", this.cidService.getVotoOmissao(user, proj));
		model.addAttribute("favor", TipoVoto.FAVOR);
		model.addAttribute("contra", TipoVoto.CONTRA);
		model.addAttribute("votarService", votarService);
		model.addAttribute("projeto", proj);

		try {
			votarService.votar(voto, proj, this.user);
			model.addAttribute("mensagem", mensagem);
		} catch (ApplicationException e) {
			mensagem = "Já votou neste projeto!";			
			model.addAttribute("mensagem", mensagem);
		} 
		return "projeto_detail";
	}

	@GetMapping("/projeto")
	public String getListaProjetos(final Model model) {
		model.addAttribute("projetos", consService.consultarProjetos());
		return "projetos_list";
	}
	
}
