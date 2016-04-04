package fi.csc.shibboleth.mobileauth.impl.authn.xmlobject;

import org.opensaml.core.xml.config.AbstractXMLObjectProviderInitializer;

/**
 * XMLObject provider initializer
 */
public class XMLObjectProviderInitializer extends AbstractXMLObjectProviderInitializer {

    /** Config resources. */
    private static String[] configs = { "/kapa-authn-extension-config.xml" };

    /** {@inheritDoc} */
    protected String[] getConfigResources() {
	return configs;
    }

}
