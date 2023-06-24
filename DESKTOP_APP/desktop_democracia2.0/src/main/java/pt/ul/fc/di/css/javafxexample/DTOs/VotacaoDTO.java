package pt.ul.fc.di.css.javafxexample.DTOs;


public class VotacaoDTO {
    private Long id;
    private String estado;
    private String projeto_id;
    private String limite;
    private int votosFavor;
    private int votosContra;
    
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getProjeto_id() {
		return projeto_id;
	}
	public void setProjeto_id(String projeto_id) {
		this.projeto_id = projeto_id;
	}
	public String getLimite() {
		return limite;
	}
	public void setLimite(String limite) {
		this.limite = limite;
	}
	public int getVotosFavor() {
		return votosFavor;
	}
	public void setVotosFavor(int votosFavor) {
		this.votosFavor = votosFavor;
	}
	public int getVotosContra() {
		return votosContra;
	}
	public void setVotosContra(int votosContra) {
		this.votosContra = votosContra;
	}
}
