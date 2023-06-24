package pt.ul.fc.css.example.business.entities;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import org.springframework.lang.NonNull;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public final class Voto {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;


    @Enumerated(EnumType.STRING)
    private TipoCidadao tipoCidadao;
    

    @Enumerated(EnumType.STRING)
    private TipoVoto tipoVoto;
    
    @ManyToOne
    @JoinColumn(name = "cidadao_id")
    private Cidadao cidadao;
    
    @ManyToOne
    @JoinColumn(name = "votacao_id")
	private Votacao votacao;
    
    public Voto(@NonNull Cidadao cidadao, TipoVoto tipovoto) {
        this.cidadao = cidadao;        
        this.tipoCidadao=this.cidadao.getTipoCidadao();
        this.tipoVoto = tipovoto;        
    }
    
    //JPA Constructor
    public Voto() {
    	
    }
    
    public TipoCidadao getTipoCidadao() {
		return tipoCidadao;
	}

	public TipoVoto getTipoVoto() {
		return tipoVoto;
	}

	public Cidadao getCidadao() {
		return cidadao;
	}

	public Votacao getVotacao() {
		return votacao;
	}	

    public Long getId() {
        return id;
    }
    
    public void setVotacao(Votacao votacao) {
    	this.votacao = votacao;
    }


}
