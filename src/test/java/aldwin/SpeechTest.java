package aldwin;

import jgodara.ai.eivor.language.LanguageProcessor;

public class SpeechTest {

	public static void main(String[] args) {
		LanguageProcessor.init();
		
		LanguageProcessor.parse("What's Pizza?");
		LanguageProcessor.parse("What can you do?");
		LanguageProcessor.parse("What do you do?");
		LanguageProcessor.parse("Can you set an alarm?");
		LanguageProcessor.parse("What do you know?");
	}

}
