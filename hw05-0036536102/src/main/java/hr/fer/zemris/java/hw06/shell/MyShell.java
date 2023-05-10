package hr.fer.zemris.java.hw06.shell;

/**
 * Razred koji implementira program koji ima funkciju
 * pojednostavljene verzije koristenja terminala.
 */
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class MyShell {

	/**
	 * Mapa koja sadrzi sve komande dostupne u programu.
	 */
	static SortedMap<String, ShellCommand> commands = new TreeMap<>((a,b) -> a.compareTo(b));

	public static void main(String[] args) {
		
		/**
		 * Sluzi za komuniciranje skorisnikom preko terminala.
		 */
		Environment environment = new Environment() {
            char PROMPTSYMBOL = '>';
            char MORELINESSYMBOL = '\\';
            char MULTILINESYMBOL = '|';
			public String readLine() throws ShellIOException {
				try {
					Scanner sc= new Scanner(System.in);
					return sc.nextLine();
				} catch(Exception e) {
					throw new ShellIOException();
				}
				
			}

			/**
			 * Ispisuje predani tekst.
			 */
			public void write(String text) throws ShellIOException {
				try {
					System.out.print(text);
				} catch(Exception e) {
					throw new ShellIOException();
				}
				
			}

			/**
			 * Ispisuje predani tekst i dodaje \n.
			 */
			public void writeln(String text) throws ShellIOException {
				try {
					System.out.println(text);
				} catch(Exception e) {
					throw new ShellIOException();
				}
				
			}

			/**
			 * Vraca sve dostupne komande.
			 */
			public SortedMap<String, ShellCommand> commands() {
				return MyShell.commands;
			}

			/**
			 * Vraca multiline simbol.
			 */
			public Character getMultilineSymbol() {
				return this.MULTILINESYMBOL;
			}

			/**
			 * Postavlja multiline simbol.
			 */
			public void setMultilineSymbol(Character symbol) {
				this.MULTILINESYMBOL = symbol;
				
			}

			/**
			 * Vraca prompt simbol.
			 */
			public Character getPromptSymbol() {
				return this.PROMPTSYMBOL;
			}

			/**
			 * Postavlja prompt simbol.
			 */
			public void setPromptSymbol(Character symbol) {
				this.PROMPTSYMBOL = symbol;
			}

			/**
			 * Vraca morelines simbol.
			 */
			public Character getMorelinesSymbol() {
				return this.MORELINESSYMBOL;
			}

			/**
			 * Postavlja morelines simbol.
			 */
			public void setMorelinesSymbol(Character symbol) {
				this.MORELINESSYMBOL = symbol;
				
			}
			
		};
		
		commands.put("exit", new ExitCommand());
		commands.put("ls", new LsCommand());
		commands.put("cat", new CatCommand());
		commands.put("charsets", new CharsetsCommand());
		commands.put("copy", new CopyCommand());
		commands.put("hexdump", new HexdumpCommand());
		commands.put("mkdir", new MkdirCommand());
		commands.put("symbol", new SymbolCommand());
		commands.put("tree", new TreeCommand());
		commands.put("help", new HelpCommand());
		
		ShellStatus status = ShellStatus.CONTINUE;
		
		environment.writeln("Welcome to MyShell v 1.0\n");
		
		while(status == ShellStatus.CONTINUE) {
			try {
			environment.write(environment.getPromptSymbol().toString()+" ");
			String s = environment.readLine();
			s = s.trim();
			while(s.endsWith(environment.getMorelinesSymbol().toString())) {
				environment.write(environment.getMultilineSymbol().toString()+" ");
				s = s.trim().substring(0,s.length()-1);
				s = s +" " + environment.readLine();
			}
			String[] redak = s.split(" ");
			String predaj = s.substring(redak[0].length());
			ShellCommand shel = commands.get(redak[0]);
			status = shel.executeCommand(environment, predaj);
			} catch(ShellIOException e) {
				status = ShellStatus.TERMINATE;
			} catch(Exception e) {
				environment.write("Neispravna komanda\n");
			}
		}
		//System.exit(0);
	}

}
