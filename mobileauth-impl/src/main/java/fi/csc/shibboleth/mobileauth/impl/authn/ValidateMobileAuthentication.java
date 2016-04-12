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

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.security.auth.Subject;

import org.opensaml.profile.action.ActionSupport;
import org.opensaml.profile.context.ProfileRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.csc.shibboleth.mobileauth.api.authn.context.MobileContext;
import fi.csc.shibboleth.mobileauth.api.authn.context.MobileContext.ProcessState;
import net.shibboleth.idp.authn.AbstractValidationAction;
import net.shibboleth.idp.authn.AuthnEventIds;
import net.shibboleth.idp.authn.context.AuthenticationContext;
import net.shibboleth.idp.authn.principal.UsernamePrincipal;
import net.shibboleth.utilities.java.support.primitive.StringSupport;

/**
 * This action will validate an {@link MobileContext} and produces an
 * {@link net.shibboleth.idp.authn.AuthenticationResult}
 * 
 * @author korteke
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ValidateMobileAuthentication extends AbstractValidationAction {

    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(ValidateMobileAuthentication.class);

    /** Context containing the result to validate. */
    @Nullable
    private MobileContext mobCtx;

    /** {@inheritDoc} */
    @Override
    protected boolean doPreExecute(@Nonnull final ProfileRequestContext profileRequestContext,
            @Nonnull final AuthenticationContext authenticationContext) {

        mobCtx = authenticationContext.getSubcontext(MobileContext.class);
        if (mobCtx == null) {
            log.debug("{} No MobileContext available within authentication context", getLogPrefix());
            ActionSupport.buildEvent(profileRequestContext, AuthnEventIds.INVALID_AUTHN_CTX);
            return false;
        }

        return true;
    }

    /** {@inheritDoc} */
    @Override
    protected void doExecute(@Nonnull final ProfileRequestContext profileRequestContext,
            @Nonnull final AuthenticationContext authenticationContext) {

        if (StringSupport.trimOrNull(mobCtx.getMobileNumber()) == null || mobCtx.getAttributes().isEmpty()) {
            log.debug("Context doesn't contain mobileNumber or attribute statement is empty");
            handleError(profileRequestContext, authenticationContext, AuthnEventIds.NO_CREDENTIALS,
                    AuthnEventIds.NO_CREDENTIALS);
            return;
        } else if (StringSupport.trimOrNull(mobCtx.getErrorMessage()) == null
                && mobCtx.getProcessState() == ProcessState.COMPLETE && !mobCtx.getAttributes().isEmpty()) {
            log.debug("{} Authentication completed. Building AuthenticationResult for {}", getLogPrefix(),
                    mobCtx.getMobileNumber());
            buildAuthenticationResult(profileRequestContext, authenticationContext);
        } else {
            handleError(profileRequestContext, authenticationContext, AuthnEventIds.NO_CREDENTIALS,
                    AuthnEventIds.NO_CREDENTIALS);
            log.debug("{} Error handling. {} for {}", getLogPrefix(), AuthnEventIds.NO_CREDENTIALS,
                    mobCtx.getMobileNumber());
            return;
        }

    }

    /** {@inheritDoc} */
    @Override
    @Nonnull
    protected Subject populateSubject(@Nonnull final Subject subject) {
        if (StringSupport.trimOrNull(mobCtx.getMobileNumber()) != null) {
            log.debug("{} Populate subject for {}", getLogPrefix(), mobCtx.getMobileNumber());
            subject.getPrincipals().add(new UsernamePrincipal(mobCtx.getMobileNumber()));
        }
        return subject;
    }

}
