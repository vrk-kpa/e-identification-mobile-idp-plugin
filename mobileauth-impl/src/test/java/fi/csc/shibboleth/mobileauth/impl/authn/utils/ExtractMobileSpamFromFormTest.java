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

    /** The Mobile number attribute. */
    private String mobileField = "j_mobileNumber";

    /** The Spam code attribute. */
    private String spamField = "j_spamcode";

    /** {@inheritDoc} */
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
        action = new ExtractMobileSpamFromForm();
        action.setmobileNumberField(mobileField);
        action.setspamCodeField(spamField);
        action.initialize();
    }

    /**
     * Initializes the expected context variables.
     */
    @BeforeTest
    public void initTest() {
        expectedMobile = "+35840123456";
        expectedSpamCode = "A1234";
        
    }

    @Test
    public void testNoServlet() throws ComponentInitializationException {

        final Event event = action.execute(src);
        ActionTestingSupport.assertEvent(event, EventIds.INVALID_PROFILE_CTX);
    }

}
