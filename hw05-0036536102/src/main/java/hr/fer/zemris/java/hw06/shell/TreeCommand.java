package hr.fer.zemris.java.hw06.shell;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Razred predstavlja implementaciju komande.
 * @author Petra
 *
 */
public class TreeCommand implements ShellCommand{

	/**
	 * Izvrsava komandu kojom se ispisuje stablo fileova djece od predanog direktorija.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		arguments = arguments.trim();
		if(arguments.startsWith("\"")) {
			if(!arguments.endsWith("\"")) {
				env.writeln("Unesen neispravan argument");
				return ShellStatus.CONTINUE;
			}
			arguments = arguments.substring(arguments.indexOf("\"")+1,arguments.lastIndexOf("\""));
		}
		Path p = Paths.get(arguments);
		File f = p.toFile();
		if(!f.exists()) {
			env.writeln("File nepostojec");
			return ShellStatus.CONTINUE;
		}
		listaj(env,f,0);
		return ShellStatus.CONTINUE;
	}

	/**
	 * Ispisuje predani file i svu djecu koja su fileovi,
	 * za djecu direktorije nastavlja se rekurzija.
	 */
	private void listaj(Environment env,File f, int i) {
		if(i == 0) {
			env.writeln(f.toString());
		}
		else {
			env.writeln(String.format("%"+i+"s%s%n", "", f.getName()));
		}
		File[] djeca = f.listFiles();
		if(djeca == null) {
			return;
		}
		i +=2;
		for(File d:djeca) {
			if(d.isFile()) {
				env.writeln(String.format("%"+i+"s%s%n", "", f.getName()));
			}
			else if(d.isDirectory()) {
				listaj(env,d,i);
			}
		}
		   
	}

	/**
	 * Vraca ime komande.
	 */
	@Override
	public String getCommandName() {
		return "tree";
	}

	/**
	 * Vraca listu koja sadrzi opis komande.
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> list = new LinkedList<String>();
		list.add("takes directory path as argument");
		list.add("lists tree");
		list = Collections.unmodifiableList(list);
		return list;
	}

}
