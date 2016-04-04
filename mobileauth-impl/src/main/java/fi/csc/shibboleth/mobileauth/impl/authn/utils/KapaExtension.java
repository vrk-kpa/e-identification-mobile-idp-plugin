package fi.csc.shibboleth.mobileauth.impl.authn.utils;

import org.opensaml.core.xml.AttributeExtensibleXMLObject;
import org.opensaml.core.xml.ElementExtensibleXMLObject;

import javax.xml.namespace.QName;

public interface KapaExtension extends ElementExtensibleXMLObject, AttributeExtensibleXMLObject {

    /** Default element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "kapa";
    
    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName("urn:kapa:SAML:2.0:extensions", "kapa");

}
