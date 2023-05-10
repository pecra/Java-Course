package hr.fer.oprpp1.hw04.db;

import java.util.LinkedList;
import java.util.List;

/**
 * Predstavlja implementaciju IFiltera koja prihvaca 
 * StudentRecords koji zadovoljavaju sve izraze iz liste predane u konsturktoru.
 * @author Petra
 *
 */
public class QueryFilter implements IFilter{
	
	private List<ConditionalExpression> list = new LinkedList<>();
	
	public QueryFilter(List<ConditionalExpression> list) {
		this.list = list;
	}
   
	/**
	 * Prihvaca Record koji zadovoljava sve izraze iz liste predane u konsturktoru.
	 */
	@Override
	public boolean accepts(StudentRecord record) {
		
		boolean accepts = true;
		
		for(ConditionalExpression e : list) {

			IFieldValueGetter ge = e.getFieldGetter();
			String rec = null;
			if(ge == FieldValueGetters.FIRST_NAME) {
				rec = record.firstName;
			}
			else {
				if(ge == FieldValueGetters.LAST_NAME) {
					rec = record.lastName;
				}
				else {
					rec = record.jmbag;
				}
			}
			if(!(e.getComparisonOperator().satisfied(rec, e.getStringLiteral()))) {
				accepts = false;
				break;
			}
		}
		return accepts;
	}

}
