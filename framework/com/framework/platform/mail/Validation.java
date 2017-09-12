package com.framework.platform.mail;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Validation {

	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		String[] toList=email.split(";");
		for (String to :toList){
		try {			
				InternetAddress emailAddress = new InternetAddress(to);
				emailAddress.validate();	
			
		} catch (AddressException ex) {
			result = false;
		}
		}
		return result;
	}

}
