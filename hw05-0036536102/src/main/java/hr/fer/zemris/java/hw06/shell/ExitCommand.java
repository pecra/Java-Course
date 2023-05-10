package hr.fer.zemris.java.hw06.shell;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
/**
 * Razred predstavlja komandu zavrsetka programa.
 * @author Petra
 *
 */
public class ExitCommand implements ShellCommand{

	/**
	 * Izvrasava komandu.
	 */
	@Override
	public ShellStatus executeCommand(Environment env, String arguments) {
		return ShellStatus.TERMINATE;
	}

	/**
	 * Vraca naziv komande.
	 */
	@Override
	public String getCommandName() {
		return "exit";
	}

	/**
	 * Vraca listu opisa koamnde.
	 */
	@Override
	public List<String> getCommandDescription() {
		List<String> list = new LinkedList<String>();
		list.add("terminates program");
		list = Collections.unmodifiableList(list);
		return list;
	}

}
