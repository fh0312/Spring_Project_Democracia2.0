package pt.ul.fc.css.example.facade.exceptions;

public class TemaNotFoundException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public TemaNotFoundException(String message) {
		super(message);
	}
	
	public TemaNotFoundException(String message, Exception e) {
		super(message, e);
	}
}
