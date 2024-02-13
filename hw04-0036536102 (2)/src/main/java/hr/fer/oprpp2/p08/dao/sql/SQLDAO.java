package hr.fer.oprpp2.p08.dao.sql;

import hr.fer.oprpp2.p08.Data;
import hr.fer.oprpp2.p08.Poll;
import hr.fer.oprpp2.p08.dao.DAO;
import hr.fer.oprpp2.p08.dao.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.taglibs.standard.tag.common.xml.ExprSupport;

/**
 * Ovo je implementacija podsustava DAO uporabom tehnologije SQL. Ova
 * konkretna implementacija očekuje da joj veza stoji na raspolaganju
 * preko {@link SQLConnectionProvider} razreda, što znači da bi netko
 * prije no što izvođenje dođe do ove točke to trebao tamo postaviti.
 * U web-aplikacijama tipično rješenje je konfigurirati jedan filter 
 * koji će presresti pozive servleta i prije toga ovdje ubaciti jednu
 * vezu iz connection-poola, a po zavrsetku obrade je maknuti.
 *  
 * @author marcupic
 */
public class SQLDAO implements DAO {


	@Override
	public List<Poll> getPolls() throws DAOException {
		List<Poll> unosi = new LinkedList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select * from Polls");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						Poll p = new Poll(null,null,null);
						p.setTitle(rs.getString(2));
						p.setLink(rs.getString(3));
						p.setId(rs.getString(1));
						unosi.add(p);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}
		return unosi;
	}

	@Override
	public List<Data> getData(String id) throws DAOException {
		List<Data> unosi = new LinkedList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select * from PollOptions where pollID ="+id);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs!=null && rs.next()) {
						Data p = new Data(rs.getString(2),rs.getString(3),rs.getString(1),rs.getString(5));
						unosi.add(p);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception ex) {
			throw new DAOException("Pogreška prilikom dohvata liste korisnika.", ex);
		}
		return unosi;
	}

	@Override
	public void setVotes(String ind) {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("UPDATE POLLOPTIONS\n"
					+ "SET votesCount = votesCount + 1\n"
					+ "WHERE id = "+ind);
			pst.executeUpdate();
		} catch(Exception ex) {
		}
	}
}