package pt.ul.fc.css.example.business.entities;

/**
 * @author
 * Alexandre Müller 	- fc56343
 * Diogo Ramos 			- fc56308
 * Francisco Henriques 	- fc56348
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.lang.NonNull;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Votacao {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private EstadoVotacao estado;
    
    @OneToOne(mappedBy = "votacao")
	private ProjetoLei projeto;
    
    @ManyToMany(fetch = FetchType.EAGER)
	private List<Voto> votosDelegados;
    
    @ManyToMany(fetch = FetchType.EAGER) 
	private List<Cidadao> votosCidadao;
    
    @Column(nullable = false)
    private Date limite;
    
    @Column(nullable=false)
    private int votosFavor;
    
    @Column(nullable=false)
    private int votosContra;
    
    public Votacao(@NonNull ProjetoLei pl, @NonNull Date data, Voto votoDelegado) {
       this.estado= EstadoVotacao.ABERTA;
       this.projeto = pl;
       this.votosDelegados = new ArrayList<Voto>();
       this.votosCidadao = new ArrayList<Cidadao>();
       this.votosDelegados.add(votoDelegado);
       this.votosCidadao.add(votoDelegado.getCidadao());
       this.limite = data;
       this.votosContra = 0;
       this.votosFavor = 1;
    }
    
    /**
     * JPA Constructor
     */
    public Votacao() {
    	
    }

    public Long getId() {
        return id;
    }

	public EstadoVotacao getEstado() {
		return estado;
	}

	public Date getLimite() {
		return limite;
	}

	public void setEstado(EstadoVotacao estado) {
		this.estado = estado;
	}

    public List<Cidadao> getVotantes(){
    	return this.votosCidadao;
    }

   public Tema getTema() {
	   return this.projeto.getTema();
   }
   
   public List<Voto> getVotosDelegados(){
   	return this.votosDelegados;
   }
   
   public void votar(TipoVoto tv, Cidadao c) {
	   this.votosCidadao.add(c);
	   if(tv.equals(TipoVoto.FAVOR)) {
		   this.votosFavor++;
	   } 
	   else if(tv.equals(TipoVoto.CONTRA)) {
		   this.votosContra++;
	   }
   }

   public boolean encerrar() {	
	   if(this.votosFavor > this.votosContra) {		   
		   this.estado = EstadoVotacao.FECHADA;				
		   return true;
	   } else {
		   this.estado = EstadoVotacao.FECHADA;
		   return false;
	   }
   }
   
   public ProjetoLei getProj() {
	   return this.projeto;
   }
   
   public void votar(Voto v, Delegado d) {
	   this.votosDelegados.add(v);
	   this.votosCidadao.add(d);
	   if(v.getTipoVoto().equals(TipoVoto.FAVOR)) {
		   this.votosFavor++;
	   } 
	   else if(v.getTipoVoto().equals(TipoVoto.CONTRA)) {
		   this.votosContra++;
	   }
   }

public int getVotosFavor() {
	return votosFavor;
}

public int getVotosContra() {
	return votosContra;
}
}
