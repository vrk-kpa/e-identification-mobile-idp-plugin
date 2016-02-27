package fi.csc.shibboleth.mobileauth.impl.authn.utils;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import fi.csc.shibboleth.mobileauth.impl.authn.Events;
import net.shibboleth.idp.authn.impl.PopulateAuthenticationContextTest;

//TODO: Stupid extend. First that came to my mind 
public class EventTest extends PopulateAuthenticationContextTest {

    /** The action to be tested. */
    private Events action;

    /** {@inheritDoc} */
    @BeforeMethod
    public void setUp() throws Exception {
        super.setUp();
    }

    /** Testing events */
    // Just testing. Basically really stupid test
    @SuppressWarnings("static-access")
    @Test
    public void testSuccess() {
        Assert.assertNotEquals(action.success.event(this).toString(), action.failure.event(this).toString());
        Assert.assertSame(action.success.event(this).toString(), action.success.event(this).toString());

    }

}
