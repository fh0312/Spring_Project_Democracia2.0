package pt.ul.fc.css.example.facade.exceptions;

public class IncorrectDateException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public IncorrectDateException(String message) {
		super(message);
	}
	
	public IncorrectDateException(String message, Exception e) {
		super(message, e);
	}
}
