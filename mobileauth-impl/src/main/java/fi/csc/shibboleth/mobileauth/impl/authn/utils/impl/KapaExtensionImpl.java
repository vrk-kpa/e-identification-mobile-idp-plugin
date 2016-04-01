package fi.csc.shibboleth.mobileauth.impl.authn.utils.impl;

import org.opensaml.core.xml.AbstractExtensibleXMLObject;

import fi.csc.shibboleth.mobileauth.impl.authn.utils.KapaExtension;

public class KapaExtensionImpl extends AbstractExtensibleXMLObject implements KapaExtension {

    public KapaExtensionImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
	super(namespaceURI, elementLocalName, namespacePrefix);
    }

}
