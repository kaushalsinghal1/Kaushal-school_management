package com.framework.platform.mail;


public class Mail {

	public boolean sendSimpleMail(String from, String password,
			String toAddress, String subject, String message,String[] attachmentsFiles) {
		boolean result = true;
		String[] toList = toAddress.split(";");

		// String to = "shafeermhd@yahoo.com";
		// /String subject = "Asalamu Alaikum";
		// String message =
		// "Dear Sister\n This is an Automated Message created by my program. \nThis  is  my second email  using my program .  First email i have  sent to you already.\n Take care my dear sister,sleep well\nYours Mohammed ShafeerIbn Abdul Nazar";
	//	for (String to : toList) {
			SendMail sendMail = new SendMail(from, password, toList,
					subject, message,attachmentsFiles);
			try {
				sendMail.send();
			} catch (Exception e) {
				result = false;
			}
		///}
		return result;
	}
}
