package hr.fer.zemris.java.hw06.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * Predstavlja komandu koja ispisuje sadrzaj filea.
 * @author Petra
 *
 */
public class CatCommand implements ShellCommand{

	/**
	 * Izvodi komandu.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments){
		arguments = arguments.trim();
		String[] args = new String[2];
		if(arguments.startsWith("\"")) {
			args[0] = arguments.substring(arguments.indexOf("\"")+1,arguments.lastIndexOf("\""));
		    if(arguments.length() > args[0].length()+2) {
		    	args[1] = arguments.substring(arguments.lastIndexOf("\"")+1);
		    	args[1] = args[1].trim();
		    }
		    else {
		    	args[1] = null;
		    }
		}
		else {
        	args = arguments.split(" ");
		}
		if(args.length < 1 || args.length > 2) {
			env.writeln("Krivo zadani cat argumenti");
			return ShellStatus.CONTINUE;
		}
		if(args.length> 1 && args[1] != null) {
			if(args[1].split(" ").length > 2) {
				env.writeln("Krivo zadani cat argumenti");
				return ShellStatus.CONTINUE;
			}
		}
		
		Path p = Paths.get(args[0]);
		Charset c = Charset.defaultCharset();
		if(args.length == 2 && args[1] != null) {
			c = Charset.forName(args[1]);
		}
		BufferedReader br;
		try {
			br = Files.newBufferedReader(p, c);
			try {
				String redak = null;
				while((redak = br.readLine()) != null) {
					env.writeln(redak);
				}
			}
			catch(IOException e) {
				env.writeln("Can't read from file");
				return ShellStatus.CONTINUE;
			}
		} catch (IOException e) {
			env.writeln("Can't read from file");
			return ShellStatus.CONTINUE;
		}
		
		return ShellStatus.CONTINUE;
	}

	/**
	 * Vraca naziv komande.
	 */
	@Override
	public String getCommandName() {
		return "cat";
	}

	/**
	 * Vraca opis komande.
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> list = new LinkedList<String>();
		list.add("takes path and charset(optional) as arguments");
		list.add("reads from file on given path usin default or given charset");
		list = Collections.unmodifiableList(list);
		return list;
	}

}
