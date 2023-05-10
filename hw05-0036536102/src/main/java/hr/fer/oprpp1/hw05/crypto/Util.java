package hr.fer.oprpp1.hw05.crypto;

public class Util {
	
	/**
	 * Pretvara nis hex znakova u polje byteova.
	 * @param keyText
	 * @return
	 */
	public static byte[] hextobyte(String keyText) {
		byte[] arr ;
		if(keyText.length()%2 != 0) {
			throw new IllegalArgumentException("Neispravan argument");
		}
		if (keyText.equals("")) {
			arr = new byte[1];
			arr[0] = Byte.valueOf("0");
			return arr;
		}
		arr = new byte[keyText.length()/2];
		keyText = keyText.toLowerCase();
		
		int temp;
		for(int i = 0, j = arr.length; i < j; i++) {
			int index = i * 2;
			String sub = keyText.substring(index, index+2);
			char[] sa = sub.toCharArray();
			if(!(sa[0] >= 'A' && sa[0] <= 'Z') && !(sa[0] >= 'a' && sa[0] <= 'z') && !(sa[0] >= '0' && sa[0] <= '9')) {
				throw new IllegalArgumentException("Neispravno zadan argument");
			}
			if(!(sa[1] >= 'A' && sa[1] <= 'Z') && !(sa[1] >= 'a' && sa[1] <= 'z') && !(sa[1] >= '0' && sa[1] <= '9')) {
				throw new IllegalArgumentException("Neispravno zadan argument");
			}
			boolean isNegative =
					sub.startsWith("8") || sub.startsWith("9") ||
					sub.startsWith("a") || sub.startsWith("b") ||
					sub.startsWith("c") || sub.startsWith("d") ||
					sub.startsWith("e") || sub.startsWith("f");
			if (isNegative) {
				  temp = Integer.parseInt(sub, 16);
			      int temp2 = 80;
			      temp = ~temp;
			      temp = temp + 1;
			      temp = temp * -1;
			} 
			else {
			      temp = Integer.parseInt(sub, 16);
			}
			arr[i] = (byte) temp;
		}
		return arr;
	}
	
	/**
	 * Vraca string koji se sastoji od hex znakova koji predstavljaju predane byteove iz polja.
	 * @param keyText
	 * @return
	 */
	public static String bytetohex(byte[] keyText) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < keyText.length; i++ ) {
	            sb.append(String.format("%02X", keyText[i]));
	        }
	    return sb.toString().toLowerCase();
	}

}
