package hr.fer.zemris.java.hw06.shell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * Razred predstavlja Komandu kopiranja iz jednog filea u drugi.
 * @author Petra
 *
 */
public class CopyCommand implements ShellCommand{

	/**
	 * Izvrsava komandu.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments){
		arguments = arguments.trim();
		String[] lista = arguments.split("\"");
		if(lista.length == 1) {
			lista = arguments.split(" ");
			if(lista.length != 2) {
				env.writeln("Unesen neispravan broj argumenata");
				return ShellStatus.CONTINUE;
			}
		}
		else {
			if(lista.length > 4 && lista.length < 2) {
				env.writeln("Unesen neispravan broj argumenata");
				return ShellStatus.CONTINUE;
			}
			int k = 0;
			for(int i = 0,j = lista.length; i<j ;i++) {
				String g = lista[i];
				g = g.trim();
				if(g != "") {
					lista[k] = g;
					k++;
				}
			}
		}
		Path from = Paths.get(lista[0]);
		Path to = Paths.get(lista[1]);
		File first = from.toFile();
		File second = to.toFile();
		if(second.exists()) {
			env.write("Should I overwrite it?[y/n]");
			String answer = env.readLine();
			if(answer.equals("n"))return ShellStatus.CONTINUE;
		}
		if(!first.exists()) {
			env.writeln("Ne postoji prvi uneseni file!");
			return ShellStatus.CONTINUE;
		}
		try {
			InputStream stream = Files.newInputStream(from);
			OutputStream stream2 = Files.newOutputStream(to);
			byte[] buff = new byte[1024];
			int r;
			while(true) {
			 r = stream.read(buff);
			 if(r<1) break;
			 stream2.write(buff);
			}
			
		} catch(IOException e) {
			env.writeln("Can't open file.");
			return ShellStatus.CONTINUE;
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Vraca naziv komande.
	 */
	@Override
	public String getCommandName() {
		return "copy";
	}

	/**
	 * Vraca listu opisa komande.
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> list = new LinkedList<String>();
		list.add("takes two paths as arguments");
		list.add("copy file from first directory to second");
		list = Collections.unmodifiableList(list);
		return list;
	}

}
