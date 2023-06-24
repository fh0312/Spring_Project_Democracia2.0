package pt.ul.fc.css.example.business.entities;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.lang.NonNull;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ProjetoLei")
@NamedQuery(name=ProjetoLei.FIND_BY_ID , query="SELECT i FROM ProjetoLei i WHERE i.id = :" + ProjetoLei.ProjetoLei_ID)
@NamedQuery(name=ProjetoLei.GET_ALL_PROJETOS , query="SELECT i FROM ProjetoLei i")
@NamedQuery(name=ProjetoLei.GET_BY_STATUS , query="SELECT i FROM ProjetoLei i WHERE i.estado = :" + ProjetoLei.ProjetoLei_STATUS)
public class ProjetoLei {

	public static final String ProjetoLei_ID = "projeto";
	public static final String FIND_BY_ID = "Projeto.findById";
	public static final String GET_ALL_PROJETOS = "ProjetoLei.getAllProjetos";
	public static final String GET_BY_STATUS = "ProjetoLei.getByStatus";
	public static final String ProjetoLei_STATUS = "estado";

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable=false)
    private String name;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoProj estado;
    
    @Column(nullable = false)
    private String texto;
    
    private File anexo;
    
    @Column(nullable = false)
    private Timestamp validade;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Tema tema;
    
    @ManyToOne
    @JoinColumn(nullable = false)
    private Delegado delegado;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "votacao_id", referencedColumnName = "id")
    private Votacao votacao;
    
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Cidadao> apoiantes;
    
    public ProjetoLei(@NonNull String name, String descText, File anexo, Timestamp ts, Tema tema, Delegado del) {
    	this.name = name;
    	this.texto = descText;
    	this.anexo = anexo;
    	this.validade = ts;
    	this.tema = tema;
    	this.delegado = del;
    	this.apoiantes = new ArrayList<Cidadao>();
    	this.estado = EstadoProj.ON_APPROVAL;
    }
    

	public static String getProjetoleiStatus() {
		return ProjetoLei_STATUS;
	}

	public EstadoProj getEstado() {
		return estado;
	}

	public String getTexto() {
		return texto;
	}

	public File getAnexo() {
		return anexo;
	}

	/**
     * JPA Constructor
     */
    public ProjetoLei() {
    	
    }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Cidadao> getApoiantes() {
		return apoiantes;
	}

	public void addApoiante(Cidadao cidadao) {
		this.apoiantes.add(cidadao);
	}
	
	public void deleteApoiante(Cidadao cidadao) {
		this.apoiantes.remove(cidadao);
	}

	public Long getId() {
		return id;
	}

	public Votacao getVotacao() {
		return votacao;
	}
    
	public EstadoProj getStatus() {
		return this.estado;
	}
	
	public void setStatus(EstadoProj estado) {
		this.estado = estado;
	}

    public int getNumApoiantes() {
    	return this.apoiantes.size();
    }
    
    public Timestamp getValidade() {
    	return this.validade;
    }
    
    public Delegado getDelegado() {
    	return this.delegado;
    }

	public void setVotacao(Votacao votacao) {
		this.votacao = votacao;
		
	}
	
	public Tema getTema() {
		return this.tema;
	}
	
	
	public boolean isOnVote() {
		return getStatus().equals(EstadoProj.ON_VOTE);
	}
	
	public boolean isOnApproval() {
		return getStatus().equals(EstadoProj.ON_APPROVAL);
	}
	
}
