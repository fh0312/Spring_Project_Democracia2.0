package pt.ul.fc.css.example.facade.exceptions;

public class InvalidDurationException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public InvalidDurationException(String message) {
		super(message);
	}
	
	public InvalidDurationException(String message, Exception e) {
		super(message, e);
	}
}
