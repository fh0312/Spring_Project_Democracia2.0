package pt.ul.fc.css.example.facade.exceptions;

public class ProjectAlreadySupportedException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public ProjectAlreadySupportedException(String message) {
		super(message);
	}
	
	public ProjectAlreadySupportedException(String message, Exception e) {
		super(message, e);
	}
}
