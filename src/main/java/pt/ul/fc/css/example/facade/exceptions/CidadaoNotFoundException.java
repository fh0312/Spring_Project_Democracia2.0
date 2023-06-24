package pt.ul.fc.css.example.facade.exceptions;

public class CidadaoNotFoundException extends ApplicationException{
	
	private static final long serialVersionUID = 1L;

	public CidadaoNotFoundException(String message) {
		super(message);
	}
	
	public CidadaoNotFoundException(String message, Exception e) {
		super(message, e);
	}

}
