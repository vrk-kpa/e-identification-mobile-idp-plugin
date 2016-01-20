package fi.csc.shibboleth.mobileauth.impl.authn;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.security.auth.Subject;

import org.opensaml.profile.action.ActionSupport;
import org.opensaml.profile.context.ProfileRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.csc.shibboleth.mobileauth.api.authn.context.MobileContext;
import net.shibboleth.idp.authn.AbstractValidationAction;
import net.shibboleth.idp.authn.AuthnEventIds;
import net.shibboleth.idp.authn.context.AuthenticationContext;
import net.shibboleth.idp.authn.principal.UsernamePrincipal;
import net.shibboleth.utilities.java.support.primitive.StringSupport;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ValidateMobileAuthentication extends AbstractValidationAction {

	/** Class logger. */
	@Nonnull
	private final Logger log = LoggerFactory.getLogger(ValidateMobileAuthentication.class);

	/** Context containing the result to validate. */
	@Nullable
	private MobileContext mobCtx;

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

	@Override
	protected void doExecute(@Nonnull final ProfileRequestContext profileRequestContext,
			@Nonnull final AuthenticationContext authenticationContext) {

		if (StringSupport.trimOrNull(mobCtx.getMobileNumber()) == null) {
			log.debug("Context doesn't contain mobileNumber");
			handleError(profileRequestContext, authenticationContext, AuthnEventIds.NO_CREDENTIALS,
					AuthnEventIds.NO_CREDENTIALS);
			return;
		}
		log.debug("Building AuthenticationResult for {}", mobCtx.getMobileNumber());
		buildAuthenticationResult(profileRequestContext, authenticationContext);
	}

	@Override
	@Nonnull
	protected Subject populateSubject(@Nonnull final Subject subject) {
		if (StringSupport.trimOrNull(mobCtx.getMobileNumber()) != null) {
			log.debug("{} Populate subject for {}", getLogPrefix(), mobCtx.getMobileNumber());
			// subject.getPrincipals().add(new
			// MobileUserPrincipal(mobCtx.getMobileNumber()));
			subject.getPrincipals().add(new UsernamePrincipal(mobCtx.getMobileNumber()));
		}
		return subject;
	}

}
