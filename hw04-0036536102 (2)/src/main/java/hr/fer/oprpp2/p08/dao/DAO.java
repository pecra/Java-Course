package hr.fer.oprpp2.p08.dao;


import java.util.List;
import java.util.Map;

import hr.fer.oprpp2.p08.Data;
import hr.fer.oprpp2.p08.Poll;

/**
 * Sučelje prema podsustavu za perzistenciju podataka.
 * 
 * @author marcupic
 *
 */
public interface DAO {

	public List<Poll> getPolls() throws DAOException;
	
	public List<Data> getData(String id) throws DAOException;
	
	public void setVotes(String ind);
	
}