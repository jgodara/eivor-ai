package jgodara.ai.eivor.startup;

import java.util.Scanner;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;
import org.fusesource.jansi.AnsiConsole;

import jgodara.ai.eivor.InputParser;
import jgodara.ai.eivor.exceptions.ParameterParseError;
import jgodara.ai.eivor.language.LanguageProcessor;

public class Eivor {
	
	public static void main(String[] args) throws ParameterParseError, Exception {
		
		LanguageProcessor.init();
		println("> Aldwin v1.0");
		println("> Ask \"What can you do?\" to start.");
		println("> Type exit to quit.");
		while (true) {
			print(">> ");
			String line = new Scanner(System.in).nextLine();
			if ("exit".equalsIgnoreCase(line)) {
				println("> Bye!");
				System.exit(0);
			} else {
				InputParser.parseInput(line).execute();
			}
		}
		
	}
	
	public static void println(Object msg) {
		AnsiConsole.systemInstall();
		System.out.println(Ansi.ansi().fg(Color.CYAN).a(msg).reset());
		AnsiConsole.systemUninstall();
	}
	
	public static void print(Object msg) {
		AnsiConsole.systemInstall();
		System.out.print(Ansi.ansi().fg(Color.CYAN).a(msg).reset());
		AnsiConsole.systemUninstall();
	}

}
