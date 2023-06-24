package pt.ul.fc.css.example.application.DTOs;


import org.springframework.stereotype.Component;

@Component
public class TemaDTO {
    private Long id;
    private String nome;
    private Long temaPai_id;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Long getTemaPai_id() {
		return temaPai_id;
	}
	public void setTemaPai_id(Long temaPai_id) {
		this.temaPai_id = temaPai_id;
	}    
}
