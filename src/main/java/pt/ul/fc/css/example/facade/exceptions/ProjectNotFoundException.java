package pt.ul.fc.css.example.facade.exceptions;

public class ProjectNotFoundException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public ProjectNotFoundException(String message) {
		super(message);
	}
	
	public ProjectNotFoundException(String message, Exception e) {
		super(message, e);
	}
}
