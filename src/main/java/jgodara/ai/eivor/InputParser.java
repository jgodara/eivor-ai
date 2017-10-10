package jgodara.ai.eivor;

import jgodara.ai.eivor.exceptions.ParameterParseError;
import jgodara.ai.eivor.language.LanguageProcessor;
import jgodara.ai.eivor.language.ParsedSpeech;
import jgodara.ai.eivor.language.SpeechType;
import jgodara.ai.eivor.processors.WhatIsCommandProcessor;
import jgodara.ai.eivor.startup.Eivor;

public class InputParser {	
	
	public static Action parseInput(String sent) {
		ParsedSpeech parsedSpeech = LanguageProcessor.parse(sent);
		if (parsedSpeech.getSpeechType() == SpeechType.WHAT || parsedSpeech.getSpeechType() == SpeechType.WHO) {
			return new Action(new WhatIsCommandProcessor(parsedSpeech.getSpeechType()), parsedSpeech.getPos(ParsedSpeech.SUBJECT));
		}
		
		return new Action(new CommandProcessor() {			
			@Override
			public void process(String argument) throws ParameterParseError, Exception {
				Eivor.println("Sorry, but I don't know how to help with that!");
			}
		}, null);
	}
	
	
}
