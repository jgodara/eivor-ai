package jgodara.ai.eivor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jgodara.ai.eivor.exceptions.ParameterParseError;
import jgodara.ai.eivor.processors.CalculateCommandProcessor;
import jgodara.ai.eivor.processors.WhatIsCommandProcessor;

public interface CommandProcessor {
	
	@SuppressWarnings("serial")
	public static final List<String> COMMANDS = new ArrayList<String>(){{
		add("calculate");
		add("what is");
	}};
	
	@SuppressWarnings("serial")
	public static final Map<String, Class<? extends CommandProcessor>> COMMANDPROCS = new HashMap<String, Class<? extends CommandProcessor>>(){{
		put("calculate", CalculateCommandProcessor.class);
		put("what is",  WhatIsCommandProcessor.class);
	}};
	
	public void process(String argument) throws ParameterParseError, Exception;

}
