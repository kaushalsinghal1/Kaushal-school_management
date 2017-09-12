package com.school.utils;


public class ValidationUtils {
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String PHONE_PATTERN = "((\\+[0-9]{2})|0)[.\\- ]?[0-9]{2,4}[.\\- ]?[0-9]{5,7}";
	private static final String MOBILE_PATTERN = "((\\+[0-9]{0,2})|0)[.\\- ]?[0-9]{10,11}";

	public static boolean isValidEmailAddress(String email) {
		// Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		// Matcher matcher = pattern.matcher(email);
		return email.matches(EMAIL_PATTERN);
	}

	public static boolean isValidPhone(String phone) {
		return phone.matches(PHONE_PATTERN);
	}
	public static boolean isValidMobile(String mobile) {
		return mobile.matches(MOBILE_PATTERN);
	}

	
	private static void phoneValidation(){
		String phone="07538-275025";
		System.out.println(""+phone+" V--->"+isValidPhone(phone));
		phone="+917538-275025";
		System.out.println(""+phone+" V--->"+isValidPhone(phone));
		phone="+917538275025";
		System.out.println(""+phone+" V--->"+isValidPhone(phone));
		phone="+917538.275025";
		System.out.println(""+phone+"V--->"+isValidPhone(phone));
		phone="++9175382-75025";
		System.out.println(""+phone+"I--->"+isValidPhone(phone));
	}
	
	private static void mobileValidation(){
		String mobile="+919926231781";
		System.out.println(""+mobile+" V--->"+isValidMobile(mobile));
		mobile="9926231781";
		System.out.println(""+mobile+" V--->"+isValidMobile(mobile));
		mobile="09926231781";
		System.out.println(""+mobile+" V--->"+isValidMobile(mobile));
		mobile="+917538888275025";
		System.out.println(""+mobile+"I--->"+isValidMobile(mobile));
		mobile="++9175382-75025";
		System.out.println(""+mobile+"I--->"+isValidMobile(mobile));
	}
	
	public static void main(String[] args) {
		String email = "kaushal@iss.co.in";
		System.out
				.println(email + "  =>is valid " + isValidEmailAddress(email));
		email = "kaushal@gmail.com";
		System.out
				.println(email + "  =>is valid " + isValidEmailAddress(email));
		email = "@kaushal@gmail.com";
		System.out
				.println(email + "  =>is valid " + isValidEmailAddress(email));
		phoneValidation();
		mobileValidation();
	}

}
