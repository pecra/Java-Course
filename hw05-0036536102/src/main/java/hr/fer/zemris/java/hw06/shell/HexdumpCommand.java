package hr.fer.zemris.java.hw06.shell;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * Razred predstavlja komandu koja ispisuje hexdump
 * sadrzaja filea.
 * @author Petra
 *
 */
public class HexdumpCommand implements ShellCommand{

	/**
	 * Izvrsava komandu.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		arguments = arguments.trim();
		if(arguments.startsWith("\"")) {
			arguments = arguments.substring(arguments.indexOf("\"")+1,arguments.lastIndexOf("\""));
		}
		Path p = Paths.get(arguments);
		File f = p.toFile();
		try {
			InputStream stream = Files.newInputStream(p);
			int act = 0;
			int line = 0;
			char[] charArray = new char[16];
			BufferedReader buffer = new BufferedReader(new InputStreamReader(stream));
			while(true) {
				charArray = new char[16];
				act = buffer.read(charArray,0,16);
				if(act == -1) {
					break;
				}
				env.write(printaj(line,charArray));
				line++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ShellStatus.CONTINUE;
	}

	/**
	 * Ispisuje redak hexdumpa.
	 * @param line
	 * @param charArray
	 * @return
	 */
	private String printaj(int line, char[] charArray) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0,j = 8-String.valueOf(line).length(); i <j;i++) {
			sb.append("0");
		}
		sb.append(String.valueOf(line));
		sb.append(":");
		for(int i = 0; i < 8; i++) {
			sb.append(" ");
			if(charArray[i] == '\u0000') {
				sb.append("  ");
			}
			else {
				String s = String.valueOf(Integer.toHexString((int)charArray[i]));
				if(s.length() < 2) {
					s = "0" + s;
				}
				sb.append(s);
			}
		}
		sb.append("|");
		for(int i = 8; i < 16; i++) {
			if(charArray[i] == '\u0000') {
				sb.append("  ");
			}
			else {
				sb.append(String.valueOf(Integer.toHexString((int)charArray[i])));
			}
			sb.append(" ");
		}
		sb.append("| ");
		for(int i = 0; i < 16; i++) {
			if(charArray[i] == '\u0000') {
				sb.append(" ");
			}
			else {
				int ja = (int)charArray[i];
				if(ja < 32 || ja > 127) {
					sb.append(".");
				}
				else {
					sb.append(charArray[i]);
				}
			}
		}
		sb.append("\n");
		return sb.toString();
	}

	/**
	 * Vraca naziv komande.
	 */
	@Override
	public String getCommandName() {
		return "hexdump";
	}

	/**
	 * Vraca listu opisa koamnde.
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> list = new LinkedList<String>();
		list.add("takes path as argument");
		list.add("writes hexdump for file on path");
		list = Collections.unmodifiableList(list);
		return list;
	}

}
