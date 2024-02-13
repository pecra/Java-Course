package hr.fer.zemris.java.tecaj_13.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Cypher {
	
	public static String process(String pass) {
		
		byte[] buff = pass.getBytes();
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        digest.update(buff, 0, buff.length);

        byte[] sum = digest.digest();

        StringBuilder sb = new StringBuilder();

        for (Byte b : sum) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
	}

}
