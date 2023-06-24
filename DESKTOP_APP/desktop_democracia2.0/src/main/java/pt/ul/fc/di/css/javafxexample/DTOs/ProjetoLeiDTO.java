package pt.ul.fc.di.css.javafxexample.DTOs;


public class ProjetoLeiDTO {

	private Long id;
	private String name;
	private String texto;
	private String validade;
	private String temaName;
	private Long delegadoId;
	private Long votacaoId;
	private Integer numApoiantes;

	public Integer getNumApoiantes() {
		return numApoiantes;
	}

	public void setNumApoiantes(Integer numApoiantes) {
		this.numApoiantes = numApoiantes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public String getValidade() {
		return validade;
	}

	public void setValidade(String validade) {
		this.validade = validade;
	}

	public String getTemaName() {
		return temaName;
	}

	public void setTemaId(String tema) {
		this.temaName = tema;
	}

	public Long getDelegadoId() {
		return delegadoId;
	}

	public void setDelegadoId(Long delegadoId) {
		this.delegadoId = delegadoId;
	}

	public Long getVotacaoId() {
		return votacaoId;
	}

	public void setVotacaoId(Long votacaoId) {
		this.votacaoId = votacaoId;
	}

}
