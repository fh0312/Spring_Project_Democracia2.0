package pt.ul.fc.css.example.facade.exceptions;

public class VotacaoAindaAbertaException extends ApplicationException{
	//Classe fora do package para não haver merge dos packages facade e exceptions
	
	private static final long serialVersionUID = 1L;

	public VotacaoAindaAbertaException(String message) {
		super(message);
	}
	
	public VotacaoAindaAbertaException(String message, Exception e) {
		super(message, e);
	}
}
