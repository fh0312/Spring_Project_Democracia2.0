package pt.ul.fc.css.example.facade.exceptions;

public class VotacaoNotFoundException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public VotacaoNotFoundException(String message) {
		super(message);
	}
	
	public VotacaoNotFoundException(String message, Exception e) {
		super(message, e);
	}
}
