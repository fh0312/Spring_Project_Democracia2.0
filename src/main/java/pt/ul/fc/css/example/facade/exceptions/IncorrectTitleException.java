package pt.ul.fc.css.example.facade.exceptions;

public class IncorrectTitleException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public IncorrectTitleException(String message) {
		super(message);
	}
	
	public IncorrectTitleException(String message, Exception e) {
		super(message, e);
	}
}
