package pt.ul.fc.css.example.facade.exceptions;

public class DelegadoNotFoundException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public DelegadoNotFoundException(String message) {
		super(message);
	}
	
	public DelegadoNotFoundException(String message, Exception e) {
		super(message, e);
	}
}
