package jgodara.ai.eivor.exceptions;

public class ParameterParseError extends RuntimeException {
	
	private static final long serialVersionUID = 8700555409801739788L;
	
	private String message;
	
	public ParameterParseError(String message) {
		this.message = message;
	}
	
	@Override
	public String getMessage() {
		return message;
	}

}
