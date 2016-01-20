package fi.csc.shibboleth.mobileauth.impl.authn.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author kkorte
 *
 */
public class MobileAuthenticationUtils {
	
	private final static String NUM_PREFIX = "+358";

	/**
	 * Validate phoneNumber against regular expression
	 * 
	 * @param number String phoneNumber that user has inputted to the form
	 * @return true if phone number is valid 
	 */
	public static boolean validatePhoneNumber(String number) {
		String pattern = "^(\\+|0)(?:[0-9] ?){6,14}[0-9]$";
		
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(number);
		
		if (m.find()) {
			System.out.println("FIND!");
			return true;
		}
		return false;
	}
	/**
	 * Fixes users phoneNumber to the correct, international form
	 * 
	 * @param number String phoneNumber that user has inputted to the form
	 * @return Fixed phoneNumber or null
	 */
	public static String fixPhoneNumber(String number) {
		
		if (number.startsWith(NUM_PREFIX)) {
			return number.trim();
			
		} else if (number.startsWith("0")) {
			StringBuilder sb = new StringBuilder("");
			number = number.substring(1);
			sb.append(NUM_PREFIX);
			sb.append(number);
			return sb.toString().trim();
		}
		return null;	
	}
	
	/**
	 * Validates spam prevention code that user has inputted to the form
	 * 
	 * @param spam String spam prevention code that user has inputted to the form
	 * @return true if spam code is valid
	 */
	public static boolean validateSpamPreventionCode(String spam) {
		String pattern = "^[a-zA-Z]\\w{3,7}$";
		
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(spam);
		
		if (m.find()) {
			return true;
		}
		
		return false;
	}
}
