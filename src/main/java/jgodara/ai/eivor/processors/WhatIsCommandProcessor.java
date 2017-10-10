package jgodara.ai.eivor.processors;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import jgodara.ai.eivor.CommandProcessor;
import jgodara.ai.eivor.exceptions.ParameterParseError;
import jgodara.ai.eivor.language.SpeechType;
import jgodara.ai.eivor.startup.Eivor;

public class WhatIsCommandProcessor implements CommandProcessor {

	private static final String WIKI_URL_FORMAT = "https://en.wikipedia.org/w/api.php?format=xml&action=query&prop=extracts&exintro=&explaintext=&titles=";
	
	private static final String[] CONJUNCTIONS = {"for", "and", "nor", "but", "or", "yet", "so"};
	private static final String[] ILLEGAL_CHARACTERS = { "/", "\n", "\r", "\t", "\0", "\f", "`", "\\?", "\\*", "\\\\", "<", ">", "|", "\"", ":", "con", "com", "prn", " "};
	
	private SpeechType speechType = SpeechType.WHAT;
	
	public WhatIsCommandProcessor(SpeechType speechType) {
		this.speechType = speechType;
	}
	
	@Override
	public void process(String argument) throws ParameterParseError, Exception {
		try {
			String wikiResponse = getResponseFromWiki(argument);
			if (wikiResponse != null && wikiResponse.trim().length() > 0) {
				Eivor.println("According to WikiPedia: " + wikiResponse);
			} else {
				wikiResponse = getResponseFromWiki(capitalize(argument));
				if (wikiResponse != null && wikiResponse.trim().length() > 0) {
					Eivor.println("According to WikiPedia: " + wikiResponse);
				} else {
					File learnTo = new File("whatisdict");
					try {
						String learnedContent = FileUtils.readFileToString(new File(learnTo, fileName(argument)));
						if (learnedContent == null || learnedContent.trim().length() == 0)
							throw new Exception();
						
						Eivor.println("> You told me that " + argument + " is " + learnedContent);
					} catch (Exception ex) {
						Eivor.println("> I don't know "+ (speechType == SpeechType.WHAT ? "what" : "who") + " " + argument + " is. Maybe you can tell me!");
						Eivor.print("(cancel or exit)>> ");
						String line = new Scanner(System.in).nextLine();
						
						if (!"exit".equalsIgnoreCase(line) && !"cancel".equalsIgnoreCase(line)) {
							if (!learnTo.exists())
								learnTo.mkdir();
							
							learnTo = new File(learnTo, fileName(argument));
							FileUtils.write(learnTo, line, false);
							
							Eivor.println("> I'll remember that now!");
						}
					}
				}
			}
		} catch (IOException ex) {
			Eivor.println("> Whoops! It looks like I dont know about " + argument + " and the internet is not available to me.");
		}
	}
	 
	private String fileName(String argument) {
		argument = argument.toLowerCase();
		for (String v : ILLEGAL_CHARACTERS) {
			argument = argument.replaceAll(v, "-");
		}
		return argument;
	}
	
	private String capitalize(String argument) {
		String[] words = argument.split(" ");
		StringBuffer cappedArg = new StringBuffer();
		for (String word : words) {
			if (Arrays.asList(CONJUNCTIONS).contains(word)) {
				cappedArg.append(" ").append(word.toLowerCase());
			} else {
				cappedArg.append(" ").append(word.substring(0, 1).toUpperCase()).append(word.substring(1).toLowerCase());
			}
		}
		
		return cappedArg.toString().trim();
	}
	
	@SuppressWarnings("deprecation")
	private String getResponseFromWiki(String argument) throws IOException, DocumentException {
		URL url = new URL(WIKI_URL_FORMAT + URLEncoder.encode(argument).replace("+", "%20"));
		
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(url);
		
		Node page = document.getRootElement().selectSingleNode("/api//query//page");
		long pageId = Long.parseLong(page.valueOf("@_idx"));
		if (pageId > -1) {
			String content = document.getRootElement().selectSingleNode("/api//query//page//extract").getText();
			content = content.substring(0, content.indexOf('.') + 1);
			
			if (content.equalsIgnoreCase("From other capitalisation: This is a redirect from a title with another method of capitalisation.")) {
				return null;
			}
			
			return content;
		}				
		
		return null;
	}

}
