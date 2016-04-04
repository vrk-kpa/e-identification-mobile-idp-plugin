package fi.csc.shibboleth.mobileauth.impl.authn;

import javax.annotation.Nonnull;

import org.opensaml.profile.action.EventIds;
import org.opensaml.profile.context.ProfileRequestContext;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

import fi.csc.shibboleth.mobileauth.api.authn.context.MobileContext;
import fi.csc.shibboleth.mobileauth.impl.authn.utils.MobileAuthenticationUtils;
import net.shibboleth.idp.authn.context.AuthenticationContext;
import net.shibboleth.idp.profile.AbstractProfileAction;
import net.shibboleth.idp.profile.ActionSupport;
import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.component.ComponentSupport;

@SuppressWarnings("rawtypes")
public class ExtractLanguageCodeFromAuthnRequest extends AbstractProfileAction {

    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(ExtractLanguageCodeFromAuthnRequest.class);

    @Nonnull
    private String ns;

    @Nonnull
    private String langTag;
    
    public void setNs(@Nonnull @NotEmpty final String fieldName) {
        log.debug("{} Adding ns to {}", getLogPrefix(), fieldName);
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        ns = fieldName;
    }
    
    public void setLangTag(@Nonnull @NotEmpty final String fieldName) {
        log.debug("{} Adding langTag to {}", getLogPrefix(), fieldName);
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        langTag = fieldName;
    }

    @Override
    protected boolean doPreExecute(ProfileRequestContext profileRequestContext) {
        log.debug("Entering ExtractLanguageCodeFromAuthnRequest doPreExecute");

        if (profileRequestContext.getSubcontext(AuthenticationContext.class, false) == null) {
            log.error("{} No authentication context", getLogPrefix());
            ActionSupport.buildEvent(profileRequestContext, EventIds.INVALID_PROFILE_CTX);
            return false;
        }

        return true;

    }

    @Override
    protected void doExecute(@Nonnull final ProfileRequestContext profileRequestContext) {

        log.debug("Entering ExtractLanguageCodeFromAuthnRequest doExecute");

        final AuthenticationContext authContext = profileRequestContext.getSubcontext(AuthenticationContext.class);
        MobileContext mobCtx = authContext.getSubcontext(MobileContext.class, true);

        final AuthnRequest authnReq = (AuthnRequest) profileRequestContext.getInboundMessageContext().getMessage();
        final Element root = authnReq.getDOM();

        final String lang = MobileAuthenticationUtils.unMarshallLanguage(root, ns, langTag);

        if (lang != null) {
            log.debug("{} Adding language [{}] to the MobileContext", getLogPrefix(), lang);
            mobCtx.setLang(lang);
            return;
        }
        
        log.debug("{} Adding default language [{}] to the MobileContext", getLogPrefix(), mobCtx.getLang());
    }
}
