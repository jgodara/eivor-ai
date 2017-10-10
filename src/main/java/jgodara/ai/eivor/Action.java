package jgodara.ai.eivor;

import jgodara.ai.eivor.exceptions.ParameterParseError;

public class Action {
	
	private CommandProcessor proc;
	private String argument;
	
	public Action(CommandProcessor proc, String argument) {
		this.proc = proc;
		this.argument = argument;
	}
	
	public void execute() throws ParameterParseError, Exception {
		proc.process(argument);
	}
	
	@Override
	public String toString() {
		return "Action" + proc;
	}

}
