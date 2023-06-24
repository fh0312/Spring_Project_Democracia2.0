package pt.ul.fc.css.example.business.entities;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Tema")
@NamedQuery(name = Tema.FIND_BY_ID, query = "SELECT t FROM Tema t WHERE t.id = :" + Tema.Tema_ID)
@NamedQuery(name = Tema.FIND_BY_NAME, query = "SELECT t FROM Tema t WHERE t.nome = :" + Tema.Tema_NAME)
public class Tema {
	
	public static final String Tema_ID = "tema";
	public static final String Tema_NAME = "temaName";
	public static final String FIND_BY_ID = "Tema.findById";
	public static final String FIND_BY_NAME = "Tema.findByName";
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;
	
	
	@Column(nullable = false, unique = true)
	private String nome;
	
	
	
	@ManyToOne
	@JoinColumn
	private Tema temaPai;	
	
	public Tema(String nome, Tema temaPai) {
		this.nome = nome;
		this.temaPai = temaPai;
	}
	
	/**
	 * JPA Constructor
	 */
	public Tema() {
		
	}
	
	public Long getId() {
		return this.id;
	}
	
	public String getName() {
		return this.nome;
	}
	
	public Tema getTemaPai() {
		return this.temaPai;
	}
	
	public List<Tema> getTemasPai(){
		List<Tema> ret = new ArrayList<Tema>();		
		Tema currentTema = this;
		while(currentTema != null) {
			if(currentTema.getTemaPai() != null) {
				ret.add(currentTema.getTemaPai());
				currentTema = currentTema.getTemaPai();
			}
			else {
				currentTema = currentTema.getTemaPai();
			}
		}
		return ret;		
	}
}
