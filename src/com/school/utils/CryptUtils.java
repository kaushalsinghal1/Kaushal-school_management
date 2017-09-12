package com.school.utils;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

//import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
//import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.apache.commons.codec.binary.Base64;

public class CryptUtils {

	private static final String ALG_NAME = "PBEWithSHA1AndDESede";
	private static final String CIP_NAME = ALG_NAME;

	private static final byte[] SALT = { 1, 2, 3, 7, 2, 15, 12, 9 };
	private static final int COUNT = 8;
	private SecretKey key;

	public CryptUtils(String password) throws InvalidKeySpecException,
			NoSuchAlgorithmException {
		SecretKeyFactory factory = SecretKeyFactory.getInstance(ALG_NAME);
		key = factory.generateSecret(new PBEKeySpec(password.toCharArray()));
	}

	public byte[] encrypt(byte[] input) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {
		Cipher cipher = Cipher.getInstance(CIP_NAME);
		cipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, COUNT));
		return cipher.doFinal(input);
	}

	public byte[] decrypt(byte[] input) throws NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidAlgorithmParameterException, IllegalBlockSizeException,
			BadPaddingException {
		Cipher cipher = Cipher.getInstance(CIP_NAME);
		cipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT, COUNT));
		return cipher.doFinal(input);
	}

	public static void main(String[] args) {
		String ENCRYPT_KEY = "fjzoia3foi";

		String password = "aa";
		byte[] encrypt;
		try {
			encrypt = new CryptUtils(ENCRYPT_KEY).encrypt(password.getBytes());
			password = new String(Base64.encodeBase64(encrypt));
			System.out.println("--encode pass-" + password);
			byte[] input = Base64.decodeBase64(password.getBytes());
			byte[] decrypt = new CryptUtils(ENCRYPT_KEY).decrypt(input);
			password = new String(decrypt);
			System.out.println("--Decrypt pass-" + password);
		} catch (InvalidKeyException e1) {
			e1.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		} catch (NoSuchPaddingException e1) {
			e1.printStackTrace();
		} catch (InvalidAlgorithmParameterException e1) {
			e1.printStackTrace();
		} catch (IllegalBlockSizeException e1) {
			e1.printStackTrace();
		} catch (BadPaddingException e1) {
			e1.printStackTrace();
		} catch (InvalidKeySpecException e1) {
			e1.printStackTrace();
		}

	}
}
