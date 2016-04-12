/*
 * The MIT License
 * Copyright (c) 2016 CSC - IT Center for Science, http://www.csc.fi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package fi.csc.shibboleth.mobileauth.impl.authn;

import java.util.Locale;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServletRequest;

import org.opensaml.profile.action.EventIds;
import org.opensaml.profile.context.ProfileRequestContext;
import org.opensaml.saml.saml2.core.AuthnRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.w3c.dom.Element;

import fi.csc.shibboleth.mobileauth.api.authn.context.MobileContext;
import fi.csc.shibboleth.mobileauth.impl.authn.utils.MobileAuthenticationUtils;
import net.shibboleth.idp.authn.context.AuthenticationContext;
import net.shibboleth.idp.profile.AbstractProfileAction;
import net.shibboleth.idp.profile.ActionSupport;
import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.component.ComponentSupport;

/**
 * This action will extract language code from the {@link AuthenticationContext}
 * 
 * @author korteke
 *
 */
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

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    protected void doExecute(@Nonnull final ProfileRequestContext profileRequestContext) {

        log.debug("Entering ExtractLanguageCodeFromAuthnRequest doExecute");

        HttpServletRequest request = getHttpServletRequest();

        final AuthenticationContext authContext = profileRequestContext.getSubcontext(AuthenticationContext.class);
        MobileContext mobCtx = authContext.getSubcontext(MobileContext.class, true);

        final AuthnRequest authnReq = (AuthnRequest) profileRequestContext.getInboundMessageContext().getMessage();
        final Element root = authnReq.getDOM();

        String lang = null;

        try {
            lang = MobileAuthenticationUtils.unMarshallLanguage(root, ns, langTag);
        } catch (Exception e) {
            log.debug("{} Can't fetch language from the request", getLogPrefix());
        }

        if (lang != null) {
            log.debug("{} Adding language [{}] to the MobileContext", getLogPrefix(), lang);
            mobCtx.setLang(lang);
            
            try {
                Locale loc = new Locale(lang);
                request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, loc);
                return;
            } catch (Exception e) {
                log.debug("{} Can't set language", getLogPrefix());
            }
            
        }

        log.debug("{} Adding default language [{}] to the MobileContext", getLogPrefix(), mobCtx.getLang());
    }

}
