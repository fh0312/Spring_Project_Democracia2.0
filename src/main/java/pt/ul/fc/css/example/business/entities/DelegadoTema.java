package pt.ul.fc.css.example.business.entities;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DelegadoTema {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "cidadao_id")
	private Cidadao cidadao;
	
	@ManyToOne
	@JoinColumn(name = "delegado_id")
	private Delegado delegado;
	
	@ManyToOne
	@JoinColumn(name = "tema_id")
	private Tema tema;
	
	
	public DelegadoTema(Tema tema, Delegado del, Cidadao cid) {
		this.cidadao=cid;
		this.delegado = del;
		this.tema=tema;
	}
	/*
	 * JPA constructor
	 */
	public DelegadoTema() {
	}
	
	
	public Delegado getDelegado() {
		return this.delegado;
	}


	public long getId() {
		return id;
	}


	public Cidadao getCidadao() {
		return cidadao;
	}


	public Tema getTema() {
		return tema;
	}
}
