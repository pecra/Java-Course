package hr.fer.oprpp2.p08;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

@WebListener
public class Inicijalizacija implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		
		Properties prop = new Properties();
		
		try {
			prop.load(sce.getServletContext().getResourceAsStream("/WEB-INF/dbsettings.properties"));
		} catch(IOException e) {
			throw new RuntimeException("Can't load properties!");
		}
		
		if (!(prop.containsKey("host") && prop.containsKey("name")&& prop.containsKey("port")
				&& prop.containsKey("user") && prop.containsKey("password"))) {
			throw new RuntimeException("Missing some properties!");
		}

		String dbName=prop.getProperty("name");
		String connectionURL = "jdbc:derby://"+prop.getProperty("host")+":"+prop.getProperty("port")+"/" + 
		dbName + ";user="+prop.getProperty("user")+";password="+prop.getProperty("password")+";create=true";
		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e1) {
			throw new RuntimeException("Pogreška prilikom inicijalizacije poola.", e1);
		}
		cpds.setJdbcUrl(connectionURL);
		
		Connection con;
		try {
			con = cpds.getConnection();
			DatabaseMetaData dbmd = con.getMetaData();
			ResultSet rs = dbmd.getTables(null, null, "%", null);
			List<String> tbl = new ArrayList<>();
			while(rs.next()) {
				tbl.add(rs.getString("TABLE_NAME"));
			}
			
			List<Data> r1 = new LinkedList<>();
			List<Data> r = new LinkedList<>();
			
			try {
				r = fetchRes(sce,"glasanje-definicija.txt", "glasanje-rezultati.txt");
				r1 = fetchRes(sce,"glasanje-definicija1.txt", "glasanje-rezultati1.txt");
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (!tbl.contains("POLLS")) {
				createTable(con, "CREATE TABLE Polls\n"
				        + " (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\n"
				        + " title VARCHAR(150) NOT NULL,\n"
				        + " message CLOB(2048) NOT NULL\n"
				        + ")");
			}
			if (!tbl.contains("POLLOPTIONS")) {
				createTable(con, "CREATE TABLE PollOptions (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, optionTitle VARCHAR(100) NOT NULL, optionLink VARCHAR(150) NOT NULL, pollID BIGINT, votesCount BIGINT, FOREIGN KEY (pollID) REFERENCES Polls(id))");

			}

			

			long id;
			boolean dod = false;
			if(noData(con,"SELECT * FROM Polls")) {
				dod = true;
				PreparedStatement ps = con.prepareStatement("INSERT INTO Polls (title,message) values (?,?)", Statement.RETURN_GENERATED_KEYS);
			    ps.setString(1, "Glasanje za omiljeni bend:");
			    ps.setString(2,  "Od sljedećih bendova, koji Vam je bend najdraži?Kliknite na link kako biste glasali!");
			    ps.executeUpdate();
			    ResultSet res = ps.getGeneratedKeys();
			    res.next();
			    id = res.getLong(1);
			    
			    ps = con.prepareStatement("INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) values (?,?,?,?)");
			    
			    for(Data d : r1) {
			    	ps.setString(1, d.getTitle());
			    	ps.setString(2, d.getLink());
			    	ps.setLong(3, id);
			    	ps.setString(4, d.getVotes());
			    	 ps.executeUpdate();
			    }
			}
			
			if(dod) {
				dod = false;
				PreparedStatement ps = con.prepareStatement("INSERT INTO Polls (title,message) values (?,?)", Statement.RETURN_GENERATED_KEYS);
			    ps.setString(1, "Glasanje za omiljenu pjesmu:");
			    ps.setString(2,"Od sljedećih pjesama, koja Vam je pjesmsa najdraža? Kliknite na link kako biste glasali!");
			    ps.executeUpdate();
			    ResultSet res = ps.getGeneratedKeys();
			    res.next();
			    id = res.getLong(1);
			    
			    ps = con.prepareStatement("INSERT INTO PollOptions (optionTitle, optionLink, pollID, votesCount) values (?,?,?,?)");
			    
			    for(Data d : r) {
			    	ps.setString(1, d.getTitle());
			    	ps.setString(2, d.getLink());
			    	ps.setLong(3, id);
			    	ps.setString(4, d.getVotes());
			    	 ps.executeUpdate();
			    }
			}	
			rs.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
		
	}

	private boolean noData(Connection con,String q) throws SQLException {
		System.out.println(q);
		PreparedStatement ps = con.prepareStatement(q);
		ResultSet rs = ps.executeQuery();
		if(rs.next()) {
			return false;
		}
		return true;
	}

	private void createTable(Connection con,String s) throws SQLException {
		System.out.println("Executing query: " + s);
		PreparedStatement ps = con.prepareStatement(s);
	    ps.executeUpdate();
	}

	private List<Data> fetchRes(ServletContextEvent sce, String string, String string2) throws IOException {
      
		String fileName = sce.getServletContext().getRealPath("/WEB-INF/"+string2);
		
		if (!Files.exists(Paths.get(fileName))) {
            Files.createFile(Paths.get(fileName));
        }
		
		List<String> lines = Files.readAllLines(Paths.get(fileName));
		Map<String,String> rez = new HashMap<>();
		for(String line : lines) {
			String[] s = line.split("\\t");
			rez.put(s[0], s[1]);
		}
		
        String fileName2 = sce.getServletContext().getRealPath("/WEB-INF/"+string);
		
		if (!Files.exists(Paths.get(fileName))) {
            Files.createFile(Paths.get(fileName));
        }
		lines = Files.readAllLines(Paths.get(fileName2));
		List<Data> ret = new LinkedList<>();
		for(String line : lines) {
			String[] s = line.split("\\t");
			ret.add(new Data(s[1], s[2], s[0], rez.get(s[0])));
		}
		ret.sort((a,b) -> b.getVotes().compareTo(a.getVotes()));
		return ret;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}