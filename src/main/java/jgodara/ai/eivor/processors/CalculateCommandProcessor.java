package jgodara.ai.eivor.processors;

import jgodara.ai.eivor.CommandProcessor;
import jgodara.ai.eivor.exceptions.ParameterParseError;
import jgodara.ai.eivor.startup.Eivor;

public class CalculateCommandProcessor implements CommandProcessor {

	@Override
	public void process(String argument) throws ParameterParseError, Exception {
		Eivor.println("> Calculating: " + argument);
	}

}
