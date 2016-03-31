package fi.csc.shibboleth.mobileauth.impl.authn.utils;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import fi.csc.shibboleth.mobileauth.impl.authn.ExtractLanguageCodeFromAuthnRequest;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;

public class ExtractLanguageCodeFromAuthnRequestTest {

    private ExtractLanguageCodeFromAuthnRequest action;
    
    private String ns;
    
    private String langTag;

    @BeforeMethod
    public void setUp() throws ComponentInitializationException {
        action = new ExtractLanguageCodeFromAuthnRequest();
        action.setNs(ns);
        action.setLangTag(langTag);
    }
    
    @BeforeTest
    public void initTest() {
        ns = "urn:kapa:SAML:2.0:extensions";
        langTag = "kapa";    
    }
    
    @Test
    public void testSuccessLangExtract() {
        // TODO
    }
    
    @Test
    public void testFailLangExtract() {
        // TODO        
    }

}
