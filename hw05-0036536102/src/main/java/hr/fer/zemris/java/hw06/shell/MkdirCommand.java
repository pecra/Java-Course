package hr.fer.zemris.java.hw06.shell;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Razred koji predstavlja komandu koja stvara direktorije predane kao path.
 * @author Petra
 *
 */
public class MkdirCommand implements ShellCommand{

	/**
	 * Izvodi komandu i vraca shell status.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		arguments = arguments.trim();
		if(arguments.startsWith("\"")) {
			if(!arguments.endsWith("\"")) {
				env.writeln("Unesen neispravan argument");
				return ShellStatus.CONTINUE;}
			arguments = arguments.substring(arguments.indexOf("\"")+1,arguments.lastIndexOf("\""));
		}
		Path p = Paths.get(arguments);
		File f = p.toFile();
		boolean made = f.mkdirs();
		if(!made) {
			env.writeln("Can't make files.");
			return ShellStatus.CONTINUE;
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Vraca naziv komande.
	 */
	@Override
	public String getCommandName() {
		return "mkdir";
	}

	/**
	 * Vraca listu opisa komande.
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> list = new LinkedList<String>();
		list.add("takes path as argument");
		list.add("creates dirs for given path");
		list = Collections.unmodifiableList(list);
		return list;
	}

}
