package pt.ul.fc.di.css.javafxexample.DTOs;


public class VotoDTO {
    private Long id;
    private String tipoCidadao;
    private String tipoVoto;
    private Long cidadao_id;
    private Long votacao_id;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTipoCidadao() {
		return tipoCidadao;
	}
	public void setTipoCidadao(String tipoCidadao) {
		this.tipoCidadao = tipoCidadao;
	}
	public String getTipoVoto() {
		return tipoVoto;
	}
	public void setTipoVoto(String tipoVoto) {
		this.tipoVoto = tipoVoto;
	}
	public Long getCidadao_id() {
		return cidadao_id;
	}
	public void setCidadao_id(Long cidadao_id) {
		this.cidadao_id = cidadao_id;
	}
	public Long getVotacao_id() {
		return votacao_id;
	}
	public void setVotacao_id(Long votacao_id) {
		this.votacao_id = votacao_id;
	}
}
