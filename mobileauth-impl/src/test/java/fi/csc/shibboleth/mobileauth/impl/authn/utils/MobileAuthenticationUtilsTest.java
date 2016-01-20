package fi.csc.shibboleth.mobileauth.impl.authn.utils;

import org.testng.annotations.Test;
import static org.testng.Assert.assertEquals;

import fi.csc.shibboleth.mobileauth.impl.authn.utils.MobileAuthenticationUtils;

public class MobileAuthenticationUtilsTest {

	private String validNumber = "+358401234567";
	private String invalidNumber = "+358123";
	private String originalNumber = "040123123";

	@Test
	public void validPhoneNumberTest() {
		assertEquals(MobileAuthenticationUtils.validatePhoneNumber(validNumber), true );
	}

	@Test
	public void invalidPhoneNumberTest() {
		assertEquals(MobileAuthenticationUtils.validatePhoneNumber(invalidNumber), false);
	}

	@Test
	public void fixPhoneNumberTest() {
		assertEquals(MobileAuthenticationUtils.fixPhoneNumber(originalNumber), "+35840123123");

	}

}
