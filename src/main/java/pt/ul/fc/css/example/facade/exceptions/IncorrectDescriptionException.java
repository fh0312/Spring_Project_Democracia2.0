package pt.ul.fc.css.example.facade.exceptions;

public class IncorrectDescriptionException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public IncorrectDescriptionException(String message) {
		super(message);
	}
	
	public IncorrectDescriptionException(String message, Exception e) {
		super(message, e);
	}
}
