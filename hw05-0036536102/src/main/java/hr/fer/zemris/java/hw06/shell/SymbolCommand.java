package hr.fer.zemris.java.hw06.shell;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Razred predstavlja komandu "symbol".
 * @author Petra
 *
 */
public class SymbolCommand implements ShellCommand{

	/**
	 * Ispisuje trenutnu reprezentaciju predanog simbola
	 * ili mijenja reprezentaciju tog simbola.
	 */
	public ShellStatus executeCommand(Environment env, String arguments) {
		arguments = arguments.trim();
		if(arguments.startsWith("PROMPT")) {
			if(arguments.length() > 6) {
                arguments = arguments.substring(6).trim();
                char[] arr = arguments.toCharArray();
                if(arr.length != 1) {
                	env.writeln("Krivo zadani prompt argumenti\n");
                	return ShellStatus.CONTINUE;
                }
                char c = env.getPromptSymbol();
                env.setPromptSymbol(arr[0]);
                env.writeln(String.format("Symbol for PROMPT changed from '%c' to '%c'\n"
                		+ "",c, env.getPromptSymbol()));
			}
			else {
				env.writeln(String.format("Symbol for PROMPT is '%c'\n", env.getPromptSymbol()));
			}
			return ShellStatus.CONTINUE;
		}
		if(arguments.startsWith("MULTILINE")) {
			if(arguments.length() > 10) {
                arguments = arguments.substring(10).trim();
                char[] arr = arguments.toCharArray();
                if(arr.length != 1) {
                	env.writeln("Krivo zadani MULTILINE argumenti\\n");
                    return ShellStatus.CONTINUE;
                }
                char c = env.getMultilineSymbol();
                env.setMultilineSymbol(arr[0]);
                env.writeln(String.format("Symbol for MULTILINE changed from '%c' to '%c'\n"
                		+ "",c, env.getMultilineSymbol()));
                return ShellStatus.CONTINUE;
			}
			else {
				env.writeln(String.format("Symbol for MULTILINE is '%c'\n", env.getMultilineSymbol()));
				return ShellStatus.CONTINUE;
			}
		}
		else {
			if(arguments.startsWith("MORELINES")) {
				if(arguments.length() > 9) {
	                arguments = arguments.substring(9).trim();
	                char[] arr = arguments.toCharArray();
	                if(arr.length != 1) {
	                	env.writeln("Krivo zadani morelines argumenti\n");
	                	return ShellStatus.CONTINUE;
	                }
	                char c = env.getMorelinesSymbol();
	                env.setMorelinesSymbol(arr[0]);
	                env.writeln(String.format("Symbol for MORELINES changed from '%c' to '%c'.\n"
	                		+ "",c, env.getMorelinesSymbol()));
	                return ShellStatus.CONTINUE;
				}
				else {
					env.writeln(String.format("Symbol for MORELINES is '%c'\n", env.getMorelinesSymbol()));
					return ShellStatus.CONTINUE;
				}
			}
			else {
				env.writeln("Nesipravna komanda\n");
				return ShellStatus.CONTINUE;
			}
		}
	}

	/**
	 * Naziv komande.
	 */
	public String getCommandName() {
		return "symbol";
	}

	/**
	 * Opis komande.
	 */
	public List<String> getCommandDescription() {
		List<String> list = new LinkedList<String>();
		list.add("symbol can be either MORELINES or PROMPT");
		list.add("write \"symbol\" + symbol to get current symbol");
		list.add("write \"symbol\" + symbol + new character to set new symbol");
		list = Collections.unmodifiableList(list);
		return list;
	}

}
