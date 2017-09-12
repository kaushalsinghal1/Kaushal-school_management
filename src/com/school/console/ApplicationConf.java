package com.school.console;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Properties;
import java.util.logging.Level;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.apache.commons.codec.binary.Base64;

import com.school.constant.ApplicationConstant;
import com.school.utils.CryptUtils;
import com.school.utils.Logger;

public class ApplicationConf {
	private static final int DEFAULT_TRIAL = 30;// days
	private int trialPeriod = DEFAULT_TRIAL;
	private static volatile ApplicationConf instance;
	private static long lastReadConf;
	String ENCRYPT_KEY = "fjzoia3foi";

	private ApplicationConf() {
	}

	public static ApplicationConf getInstance() {
		if (instance == null) {
			instance = new ApplicationConf();
		}
		return instance;
	}

	public int getTrialPeriod() {
		loadConf();
		return trialPeriod;
	}

	private void loadConf() {
		File file = new File(ApplicationConstant.LICENSE_CONF_FILE);
		if (file.lastModified() < lastReadConf) {
			return;
		}
		Properties prop = new Properties();
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			prop.load(fis);
			lastReadConf = System.currentTimeMillis();
		} catch (IOException e) {
			Logger.EXCEPTION.log(Level.WARNING,
					"Error While reading Application Configuration" + e);
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					Logger.EXCEPTION.log(Level.WARNING, "Error While closing"
							+ e);
				}
		}
		String temp = prop
				.getProperty(ApplicationConstant.TRIAL_PERIOD_PROPERTY);
		if (temp != null) {
			String value = decode(temp);
			if (value == null) {
				Logger.DEBUG.info("Invalid Trial value " + temp);
			} else {
				try {
					trialPeriod = Integer.parseInt(value);
				} catch (NumberFormatException e) {
					Logger.DEBUG.info("Invalid Value");
					Logger.DEBUG.info("Default trail period value set");
					trialPeriod = DEFAULT_TRIAL;
				}
			}
		}
	}

	public void setTrialValue(int value) {
		trialPeriod = value;
		saveConf();
	}

	private void saveConf() {
		Properties prop = new Properties();
		String temp = encode(trialPeriod + "");
		prop.setProperty(ApplicationConstant.TRIAL_PERIOD_PROPERTY,
				temp == null ? "" : temp);
		FileOutputStream fio = null;
		try {
			fio = new FileOutputStream(new File(
					ApplicationConstant.LICENSE_CONF_FILE));
			prop.store(fio, "Updated");

		} catch (IOException e) {
			Logger.EXCEPTION.log(Level.WARNING,
					"Error While Writing Application Configuration" + e);
		} finally {
			if (fio != null) {
				try {
					fio.close();
				} catch (IOException e) {
					Logger.EXCEPTION.log(Level.WARNING, "Error While closing"
							+ e);
				}
			}
		}

	}

	private String encode(String value) {
		byte[] encrypt = null;
		try {
			byte[] input = value.getBytes();// Base64.decodeBase64(value.getBytes());
			encrypt = new CryptUtils(ENCRYPT_KEY).encrypt(input);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		if (encrypt == null)
			return null;
		return new String(Base64.encodeBase64(encrypt));
	}

	private String decode(String value) {
		byte[] decrypt = null;
		try {
			byte[] input = Base64.decodeBase64(value.getBytes());
			decrypt = new CryptUtils(ENCRYPT_KEY).decrypt(input);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
		if (decrypt == null)
			return null;
		return new String(decrypt);
	}

	public static void main(String[] args) {
		System.out.println("Read Value---->"
				+ ApplicationConf.getInstance().getTrialPeriod());
		ApplicationConf.getInstance().setTrialValue(60);
		System.out.println("Read Value---->"
				+ ApplicationConf.getInstance().getTrialPeriod());

	}
}
