package jgodara.ai.eivor.language;

import java.util.HashMap;
import java.util.Map;

public class ParsedSpeech {
	
	public static final String ROOT 				= "_ROOT";
	public static final String SUBJECT				= "_SUBJ";
	public static final String F_ATTRIB_NAME 		= "F_ATTRIB";
	public static final String F_ATTRIB_VAL 		= "F_ATTRIB_VAL";
	
	private SpeechType speechType;
	private Map<String, String> speechData = new HashMap<String, String>();
	
	public ParsedSpeech(SpeechType speechType) {
		this.speechType = speechType;
	}

	public SpeechType getSpeechType() {
		return speechType;
	}

	public String getPos(String pos) {
		return speechData.get(pos);
	}

	public void addPos(String pos, String data) {
		speechData.put(pos, data);
	}

}
