package fi.csc.shibboleth.mobileauth.impl.authn.utils;

import org.opensaml.profile.action.EventIds;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.webflow.execution.Event;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import fi.csc.shibboleth.mobileauth.api.authn.context.MobileContext;
import fi.csc.shibboleth.mobileauth.impl.authn.ExtractMobileSpamFromForm;
import net.shibboleth.idp.authn.AuthnEventIds;
import net.shibboleth.idp.authn.context.AuthenticationContext;
import net.shibboleth.idp.authn.impl.PopulateAuthenticationContextTest;
import net.shibboleth.idp.profile.ActionTestingSupport;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;

public class ExtractMobileSpamFromFormTest extends PopulateAuthenticationContextTest {

    /** The action to be tested. */
    private ExtractMobileSpamFromForm action;

    /** The Mobile number attribute. */
    private String expectedMobile;

    /** The Spam code attribute. */
    private String expectedSpamCode;
    
    /** The invalid Mobile number attribute. */
    private String invalidMobile;

    /** The invalid Spam code attribute. */
    private String invalidSpam;

    /** The Mobile number field. */
    private String mobileNumberField;

    /** The Spam code field. */
    private String spamCodeField;

    /** {@inheritDoc} */
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        action = new ExtractMobileSpamFromForm();
        action.setMobileNumberField(mobileNumberField);
        action.setSpamCodeField(spamCodeField);
    }

    /**
     * Initializes the expected context variables.
     */
    @BeforeTest
    public void initTest() {
        mobileNumberField = "j_mobileNumber";
        spamCodeField = "j_spamcode";
        expectedMobile = "+35840123456";
        expectedSpamCode = "A1234";
        invalidMobile = "1231";
        invalidSpam = "1234";
        
    }

    @Test
    public void testNoServlet() throws ComponentInitializationException {
        action.initialize();
        final Event event = action.execute(src);
        ActionTestingSupport.assertEvent(event, EventIds.INVALID_PROFILE_CTX);
    }
    
    @Test
    public void testSuccess() throws ComponentInitializationException {        
        action.setHttpServletRequest(new MockHttpServletRequest());
        ((MockHttpServletRequest) action.getHttpServletRequest()).addParameter(mobileNumberField, expectedMobile);
        ((MockHttpServletRequest) action.getHttpServletRequest()).addParameter(spamCodeField, expectedSpamCode);
        action.initialize();
        prc.getSubcontext(AuthenticationContext.class, false).setAttemptedFlow(authenticationFlows.get(0));
        final AuthenticationContext authCtx = prc.getSubcontext(AuthenticationContext.class, false);
        final Event event = action.execute(src);
        Assert.assertNull(event);
        final MobileContext mobCtx = authCtx.getSubcontext(MobileContext.class, false);
        Assert.assertEquals(mobCtx.getMobileNumber(), expectedMobile);
        Assert.assertEquals(mobCtx.getSpamCode(), expectedSpamCode);
        
    }
    
    @Test
    public void testFail() throws ComponentInitializationException {
        action.setHttpServletRequest(new MockHttpServletRequest());
        ((MockHttpServletRequest) action.getHttpServletRequest()).addParameter("j_mobileNumber", invalidMobile);
        ((MockHttpServletRequest) action.getHttpServletRequest()).addParameter("j_spamcode", invalidSpam);
        action.initialize();
        prc.getSubcontext(AuthenticationContext.class, false).setAttemptedFlow(authenticationFlows.get(0));
        final Event event = action.execute(src);
        Assert.assertEquals(AuthnEventIds.INVALID_CREDENTIALS,event.toString());
    }

}
