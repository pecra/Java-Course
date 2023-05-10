package hr.fer.zemris.java.gui.layouts;
/**
 * Razred koji predstavlja poziciju na koju se postavlja nova komponenta pomocu lm.
 * @author Petra
 *
 */
public class RCPosition {
	private int row;
	private int column;
	
	public RCPosition(int row, int column) {
		this.row = row;
		this.column = column;
	}

	/**
	 * Getter
	 * @return
	 */
	public int getRow() {
		return row;
	}
	/**
	 * Getter
	 * @return
	 */
	public int getColumn() {
		return column;
	}
	/**
	 * Parsiranje iz string u RCPosition.
	 * @param s
	 * @return
	 */
	public static RCPosition parse(String s) {
		String[] arr = s.split(",");
		if(arr.length != 2) {
			throw new IllegalArgumentException();
		}
		if(Integer.valueOf(arr[0]) > 9 || Integer.valueOf(arr[0]) < 0 || Integer.valueOf(arr[0]) > 9 || Integer.valueOf(arr[0]) < 0) {
			throw new IllegalArgumentException();
		}
		return new RCPosition(Integer.parseInt(arr[0]),Integer.parseInt(arr[1]));
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) {
			if(this != null) {
				return false;
			}
			return true;
		}
		return ((RCPosition) obj).getColumn()==this.getColumn() && ((RCPosition) obj).getRow()==this.getRow();
	}
	
	
	
	

}
