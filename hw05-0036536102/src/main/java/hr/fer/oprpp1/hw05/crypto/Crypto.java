package hr.fer.oprpp1.hw05.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Crypto {

	/**
	 * Racuna digest za predani file i usporeduje s predanim digestom
	 * ili enkriptira ili dekriptira predani dokument pomocu predanog kljuca i vektora za inicijalizaciju.
	 * @param args
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws NoSuchPaddingException
	 * @throws InvalidKeyException
	 * @throws InvalidAlgorithmParameterException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
		if(args[0].equals("checksha")) {
			Scanner sc= new Scanner(System.in); 
			System.out.format("Please provide expected sha-256 digest for %s:\n", args[1]);  
			String str= sc.nextLine();
			Path p = Paths.get(args[1]);
			InputStream stream = Files.newInputStream(p);
			MessageDigest sha = MessageDigest.getInstance("SHA-256");
			byte[] buff = new byte[1024];
			while(true) {
			 int r = stream.read(buff);
			 if(r<1) break;
			 sha.update(buff,0,r);
			}
			stream.close();
			byte[] hash = sha.digest();
			String sh = Util.bytetohex(hash);
			if(sh.equals(str)) {
				System.out.format("Digesting completed. Digest of  %s matches expected digest.", args[1]); 
			}
			else {
				System.out.format("Digesting completed. Digest of %s does not match the expected digest. Digest\r\n"
						+ "was: %s", args[1],sh);
			}
		}
		else {
		boolean encrypt = false;
		if(args[0].equals("encrypt")) {
			encrypt = true;	
		}
		Scanner sc= new Scanner(System.in);
		System.out.format("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):\n"); 
		String keyText= sc.nextLine();
		System.out.format("Please provide initialization vector as hex-encoded text (32 hex-digits):\n"); 
		String ivText= sc.nextLine();
		SecretKeySpec keySpec = new SecretKeySpec(Util.hextobyte(keyText), "AES");
		AlgorithmParameterSpec paramSpec = new IvParameterSpec(Util.hextobyte(ivText));
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, keySpec, paramSpec);
		
		Path p = Paths.get(args[1]);
		InputStream stream = Files.newInputStream(p);
		Path p2 = Paths.get(args[2]);
		OutputStream stream2 = Files.newOutputStream(p2);
		byte[] buff = new byte[1024];
		int r;
		while(true) {
		 r = stream.read(buff);
		 if(r<1) break;
		 
		 byte[] blok = cipher.update(buff,0,r);
		 stream2.write(blok);
		}
		byte[] blok2 = cipher.doFinal();
		stream2.write(blok2);
		stream.close();
		stream2.close();
		if(encrypt) {
			System.out.format("Encryption completed. Generated file %s "
					+ "based on file %s.\n",args[1],args[2]); 
		}
		if(!encrypt) {
			System.out.format("Decryption completed. Generated file %s "
					+ "based on file %s.\n",args[2],args[1]);
		}
		}
		
	}

}
