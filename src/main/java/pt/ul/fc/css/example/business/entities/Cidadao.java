package pt.ul.fc.css.example.business.entities;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;

@Entity
@NamedQuery(name = Cidadao.FIND_BY_ID, query = "SELECT i FROM Cidadao i WHERE i.id = :" + Cidadao.CIDADAO_ID)
@NamedQuery(name = Cidadao.GET_ALL_CIDADAOS, query = "SELECT i FROM Cidadao i")
public class Cidadao {

	public static final String FIND_BY_ID = "Cidadao.findById";
	public static final String CIDADAO_ID = "cidadao";
	public static final String GET_ALL_CIDADAOS = "Cidadao.getAllCidadaos";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "nome", columnDefinition = "VARCHAR(255)")
	private String nome;

	@Column(name = "tel", columnDefinition = "INT")
	private int tel;

	@Column(name = "cc", columnDefinition = "VARCHAR(255)")
	private String cc;

	@Column(name = "mail", columnDefinition = "VARCHAR(255)")
	private String mail;

	@Column(name = "tipo")
	private TipoCidadao tipo;

	public Cidadao(String name, String mail, String cc, int tel) {
		this.nome = name;
		this.cc = cc;
		this.tel = tel;
		this.mail = mail;
		this.tipo = TipoCidadao.CIDADAO;
	}

	public Cidadao() {
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return nome;
	}

	public void setName(String name) {
		this.nome = name;
	}

	public int getTel() {
		return tel;
	}

	public void setTel(int tel) {
		this.tel = tel;
	}

	public String getCc() {
		return cc;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public TipoCidadao getTipoCidadao() {
		return TipoCidadao.CIDADAO;
	}

	public static String getFindById() {
		return FIND_BY_ID;
	}

	public static String getCidadaoId() {
		return CIDADAO_ID;
	}

	public static String getGetAllCidadaos() {
		return GET_ALL_CIDADAOS;
	}

	public String getNome() {
		return nome;
	}

	public TipoCidadao getTipo() {
		return tipo;
	}

	protected void setTipo(TipoCidadao tipo) {
		this.tipo = tipo;
	}

	public boolean isDelegado() {
		return this.getTipoCidadao().equals(TipoCidadao.DELEGADO);
	}

}
