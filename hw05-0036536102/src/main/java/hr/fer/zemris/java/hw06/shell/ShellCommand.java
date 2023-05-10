package hr.fer.zemris.java.hw06.shell;

import java.util.List;

/**
 * Sucelje pomocu kojeg se implementiraju komande.
 * @author Petra
 *
 */
public interface ShellCommand {

	ShellStatus executeCommand(Environment env, String arguments);
	String getCommandName();
	List<String> getCommandDescription();

}
