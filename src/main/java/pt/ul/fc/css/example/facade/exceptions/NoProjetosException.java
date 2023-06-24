package pt.ul.fc.css.example.facade.exceptions;

public class NoProjetosException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public NoProjetosException(String message) {
		super(message);
	}
	
	public NoProjetosException(String message, Exception e) {
		super(message, e);
	}
}
