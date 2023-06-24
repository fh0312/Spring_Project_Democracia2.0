package pt.ul.fc.css.example.application.DTOs;


import org.springframework.stereotype.Component;

@Component
public class CidadaoDTO {
    private Long id;
    private String name;
    private String cc;
    private int tel;
    private String mail;
    private String tipoCidadao;
    private boolean isDelegado;
    
	public boolean isDelegado() {
		return isDelegado;
	}
	public void setDelegado(boolean isDelegado) {
		this.isDelegado = isDelegado;
	}
	public String getTipoCidadao() {
		return tipoCidadao;
	}
	public void setTipoCidadao(String tipoCidadao) {
		this.tipoCidadao = tipoCidadao;
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
	public String getCc() {
		return cc;
	}
	public void setCc(String cc) {
		this.cc = cc;
	}
	public int getTel() {
		return tel;
	}
	public void setTel(int tel) {
		this.tel = tel;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
}
