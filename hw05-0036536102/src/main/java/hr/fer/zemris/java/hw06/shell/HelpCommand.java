package hr.fer.zemris.java.hw06.shell;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

/**
 * Razred predstavlja komandu koja ispisuje upute za koristernje programa.
 * @author Petra
 *
 */
public class HelpCommand implements ShellCommand{

	/**
	 * Izvrsaava komandu.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		SortedMap<String, ShellCommand> commands = env.commands();
		arguments = arguments.trim();
		if(arguments.equals("")) {
			for(Map.Entry<String, ShellCommand> entry : commands.entrySet()) {
				env.writeln(entry.getKey() + ":\n");
				List<String> l = entry.getValue().getCommandDescription();
				for(String s : l) {
					env.writeln("  " + s + "\n");
				}
			}
		}
		else {
			ShellCommand s = commands.get(arguments);
			env.writeln(arguments + ":\n");
			List<String> l = s.getCommandDescription();
			for(String t : l) {
				env.writeln("  " + t + "\n");
			}
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Vraca naziv komande.
	 */
	@Override
	public String getCommandName() {
		return "help";
	}

	/**
	 * Vraca listu opisa komande.
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> list = new LinkedList<String>();
		list.add("takes one or no arguments and writes their description");
		list = Collections.unmodifiableList(list);
		return list;
	}

}
