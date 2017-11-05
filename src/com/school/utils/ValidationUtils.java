package com.school.utils;


public class ValidationUtils {
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final String PHONE_PATTERN = "((\\+[0-9]{2})|0)[.\\- ]?[0-9]{2,4}[.\\- ]?[0-9]{5,7}";
	//private static final String MOBILE_PATTERN = "((\\+[0-9]{0,2})|0)[.\\- ]?[\\d]{10,11}";
	private static final String MOBILE_PATTERN = "^(([+]|[0]{0,2})([\\d]{1,3})([\\s-]{0,1}))?([\\d]{10})$";
	private static final String AADHAR_PATTERN="^[\\d]{12}$";


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
	
	public static boolean isValidAdhaar(String adhar) {
		return adhar !=null && adhar.matches(AADHAR_PATTERN);
	}
	
	private static void aadharValidation() {
		String aadhar="123412341234";
		System.out.println(""+aadhar+" V--->"+isValidAdhaar(aadhar));
		aadhar="1234123412ab";
		System.out.println(""+aadhar+" I--->"+isValidAdhaar(aadhar));
		aadhar="123412341211212";
		System.out.println(""+aadhar+" I--->"+isValidAdhaar(aadhar));
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
		System.out.println(""+mobile+" I--->"+isValidMobile(mobile));
		mobile="++9175382-75025";
		System.out.println(""+mobile+" I--->"+isValidMobile(mobile));
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
		aadharValidation();
	}

}
