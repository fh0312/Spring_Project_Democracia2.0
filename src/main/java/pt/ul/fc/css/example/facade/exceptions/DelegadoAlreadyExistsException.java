package pt.ul.fc.css.example.facade.exceptions;

public class DelegadoAlreadyExistsException extends ApplicationException{
	
	private static final long serialVersionUID = 1L;

	public DelegadoAlreadyExistsException(String message) {
		super(message);
	}
	
	public DelegadoAlreadyExistsException(String message, Exception e) {
		super(message, e);
	}

}
