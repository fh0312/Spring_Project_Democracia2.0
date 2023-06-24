package pt.ul.fc.css.example.facade.exceptions;

public class AlreadyVotedException extends ApplicationException{

	private static final long serialVersionUID = 1L;

	public AlreadyVotedException(String message) {
		super(message);
	}
	
	public AlreadyVotedException(String message, Exception e) {
		super(message, e);
	}
}
