package hr.fer.zemris.java.hw06.shell;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
/**
 * Razred predstavlja komandu koja ispisuje podrzane charsetse.
 * @author Petra
 *
 */
public class CharsetsCommand implements ShellCommand{

	/**
	 * Izvodi komandu.
	 */
	public ShellStatus executeCommand(Environment env, String arguments) {
		SortedMap<String,Charset> map = Charset.availableCharsets();
		map.forEach((k,v) -> env.write(k +"\n"));
		return ShellStatus.CONTINUE;
	}

	/**
	 * Vraca naziv komande.
	 */
	public String getCommandName() {
		return "charsets";
	}

	/**
	 * Vraca Opis komande.
	 */
	public List<String> getCommandDescription() {
		List<String> list = new LinkedList<String>();
		list.add("charsets command returns all supported charsets");
		list = Collections.unmodifiableList(list);
		return list;
	}

}
