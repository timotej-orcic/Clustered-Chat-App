package packages.utils;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {

	private static final Random RANDOM = new SecureRandom();
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	private static final int ITERATIONS = 1000;
	private static final int KEYLEN = 256;
	
	public static String getSalt(int length) {
		StringBuilder retVal = new StringBuilder(length);
		
		for (int i = 0; i < length; i++) {
            retVal.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }
		
        return new String(retVal);
	}
	
	public static byte[] hash(char[] pw, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(pw, salt, ITERATIONS, KEYLEN);
		Arrays.fill(pw, Character.MIN_VALUE);
		byte[] ret = null;
		
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			ret = skf.generateSecret(spec).getEncoded();
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("Greska pri kreiranju lozinke.");
		} finally {
			spec.clearPassword();
		}
		
		return ret;
	}
	
	public static String generateSecurePW(String pw, String salt) {
		String ret = null;
		
		byte[] secure = hash(pw.toCharArray(), salt.getBytes());
		ret = Base64.getEncoder().encodeToString(secure);
		
		return ret;
	}
	
	public static boolean verifyPW(String providedPW, String realPW, String salt) {
		String newSecure = generateSecurePW(providedPW, salt);
		return newSecure.equals(realPW);
	}
}
