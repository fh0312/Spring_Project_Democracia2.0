package pt.ul.fc.css.example.business.entities;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import jakarta.persistence.Entity;

@Entity
public class Delegado extends Cidadao {

	public Delegado(String name,String mail, String cc, int tel) {
		super(name,mail, cc, tel);
		this.setTipo(TipoCidadao.DELEGADO);
	}
	
	/**
     * JPA Constructor
     */
    public Delegado() {
    	super();
    	this.setTipo(TipoCidadao.DELEGADO);
    }
	
	
	

	public TipoCidadao getTipoCidadao() {
		return TipoCidadao.DELEGADO;
	}




}
