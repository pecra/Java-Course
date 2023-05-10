package hr.fer.zemris.java.hw06.shell;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Razred koji predstavlja komandu ispisa fileova u direktoriju,
 * te njihovih atributa(vrijeme i datum stvaranja,naziv i podatke o fileu.
 * @author Petra
 *
 */
public class LsCommand implements ShellCommand{

	/**
	 * Izvodi komandu.
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
		if(!Files.exists(p)) {
			env.writeln("File ne postoji");
			return ShellStatus.CONTINUE;
		}
		File f = new File(arguments);
		File[] children = f.listFiles();
		for(File file : children) {
			 StringBuilder sb = new StringBuilder();
			 sb.append(indicator(file));
			 sb.append(gerSize(file));
			 sb.append(" ");
			 try {
				sb.append(getTimeDate(arguments));
			} catch (IOException e) {
				env.writeln("Ne mogu dohvattit daum i vrijeme izrade");
				return ShellStatus.CONTINUE;
			}
			 sb.append(" ");
			 sb.append(file.getName());
			 sb.append("\n");
			 env.writeln(sb.toString());
			}
		return ShellStatus.CONTINUE;
		
		
	}

	/**
	 * Vraca datum i vrijeme izrade filea.
	 * @param p
	 * @return
	 * @throws IOException
	 */
	private String getTimeDate(String p) throws IOException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Path path = Paths.get(p);
		BasicFileAttributeView faView = Files.getFileAttributeView(
		path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS
		);
		BasicFileAttributes attributes = faView.readAttributes();
		FileTime fileTime = attributes.creationTime();
		String formattedDateTime = sdf.format(new Date(fileTime.toMillis()));
		return formattedDateTime;

	}

	/**
	 * Vraca velicinu filea.
	 * @param file
	 * @return
	 */
	private String gerSize(File file) {
	    long size = file.length();
	    String s = String.valueOf(size);
	    String ret = "";
	    for(int i = 0;i < 10-s.length();i++) {
	    	ret = ret + " ";
	    }
	    ret = ret + s;
		return ret;
	}

	/**
	 * Vraca string koji predstavlja funkcionalnosti filea.
	 * @param file
	 * @return
	 */
	private String indicator(File file) {
		String s = "";
		if(file.isDirectory()) {
			s = s + "d";
		}
		else {
			s = s + "-";
		}
		if(file.canRead()) {
			s = s + "r";
		}
		else {
			s = s + "-";
		}
		if(file.canWrite()) {
			s = s + "w";
		}
		else {
			s = s + "-";
		}
		if(file.canExecute()) {
			s = s + "x";
		}
		else {
			s = s + "-";
		}
		return s;
	}

	/**
	 * Vraca naziv komande.
	 */
	@Override
	public String getCommandName() {
		return "ls";
	}

	/**
	 * Vraca listu opisa komande.
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> list = new LinkedList<String>();
		list.add("takes directory path as argument");
		list.add("lists all children with following attributes:");
		list.add("first column - directory/readable/writeable/executable");
		list.add("second column - size");
		list.add("third column - date/time");
		list = Collections.unmodifiableList(list);
		return list;
	}

}
