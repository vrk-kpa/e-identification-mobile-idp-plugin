package fi.csc.shibboleth.mobileauth.impl.authn;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.annotation.Nonnull;
import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.opensaml.profile.action.EventIds;
import org.opensaml.profile.context.ProfileRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fi.csc.shibboleth.mobileauth.api.authn.StatusResponse;
import fi.csc.shibboleth.mobileauth.api.authn.context.MobileContext;
import fi.csc.shibboleth.mobileauth.api.authn.context.MobileContext.ProcessState;
import net.shibboleth.idp.authn.AbstractAuthenticationAction;
import net.shibboleth.idp.authn.context.AuthenticationContext;
import net.shibboleth.idp.profile.ActionSupport;
import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.primitive.StringSupport;

@SuppressWarnings("rawtypes")
public class AuthenticateMobile extends AbstractAuthenticationAction {

	/** Class logger. */
	@Nonnull
	private final static Logger log = LoggerFactory.getLogger(AuthenticateMobile.class);

	@Nonnull
	private String mobileNumber;

	@Nonnull
	private String spamCode;

	@Nonnull
	private  String authServer;

	@Nonnull
	private  int authPort;

	@Nonnull
	private  String authPath;

	@Nonnull
	private  String keystoreType;
	
	@Nonnull
	private String keystorePath;
	
	@Nonnull
	private String keystorePasswd;
	
	@Nonnull
	private String keyPasswd;
	
	@Nonnull
	private String keyAlias;

	@Nonnull
	private String trustStorePath;
	
	@Nonnull
	private String trustStoreType;

	@Nonnull
	private String trustStorePassword;

	@Nonnull
	private MobileContext mobCtx;
	
	private CloseableHttpClient httpClient;

	private CloseableHttpResponse response = null;

	/* TODO: Create just one constructor to initialize these variables.. */
	public void setauthServer(@Nonnull @NotEmpty final String fieldName) {
		log.debug("{} Adding authServer to {}", getLogPrefix(), fieldName);
		ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
		authServer = Constraint.isNotNull(StringSupport.trimOrNull(fieldName),
				"authServer field name cannot be null or empty.");
	}

	public void setauthPort(@Nonnull @NotEmpty final int fieldName) {
		log.debug("{} Adding authPort to {}", getLogPrefix(), fieldName);
		ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
		authPort = fieldName;
	}

	public void setauthPath(@Nonnull @NotEmpty final String fieldName) {
		log.debug("{} Adding authPath to {}", getLogPrefix(), fieldName);
		ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
		authPath = Constraint.isNotNull(StringSupport.trimOrNull(fieldName),
				"authPath field name cannot be null or empty.");
	}

	public void setkeystoreType(@Nonnull @NotEmpty final String fieldName) {
		log.debug("{} Adding keystoreType to {}", getLogPrefix(), fieldName);
		ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
		keystoreType = Constraint.isNotNull(StringSupport.trimOrNull(fieldName),
				"keystoreType field name cannot be null or empty.");
	}

	public void setkeystorePath(@Nonnull @NotEmpty final String fieldName) {
		log.debug("{} Adding keystorePath to {}", getLogPrefix(), fieldName);
		ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
		keystorePath = Constraint.isNotNull(StringSupport.trimOrNull(fieldName),
				"keystorePath field name cannot be null or empty.");
	}

	public void setkeystorePasswd(@Nonnull @NotEmpty final String fieldName) {
		log.debug("{} Adding keystorePasswd to {}", getLogPrefix(), fieldName);
		ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
		keystorePasswd = Constraint.isNotNull(StringSupport.trimOrNull(fieldName),
				"keystorePasswd field name cannot be null or empty.");
	}

	public void setkeyPasswd(@Nonnull @NotEmpty final String fieldName) {
		log.debug("{} Adding keyPasswd to {}", getLogPrefix(), fieldName);
		ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
		keyPasswd = Constraint.isNotNull(StringSupport.trimOrNull(fieldName),
				"keyPasswd field name cannot be null or empty.");
	}

	public void setkeyAlias(@Nonnull @NotEmpty final String fieldName) {
		log.debug("{} Adding keyAlias to {}", getLogPrefix(), fieldName);
		ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
		keyAlias = Constraint.isNotNull(StringSupport.trimOrNull(fieldName),
				"keyAlias field name cannot be null or empty.");
	}

	public void settrustStorePath(@Nonnull @NotEmpty final String fieldName) {
		log.debug("{} Adding trustStorePath to {}", getLogPrefix(), fieldName);
		ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
		trustStorePath = Constraint.isNotNull(StringSupport.trimOrNull(fieldName),
				"trustStorePath field name cannot be null or empty.");
	}

	public void settrustStoreType(@Nonnull @NotEmpty final String fieldName) {
		log.debug("{} Adding trustStoreType to {}", getLogPrefix(), fieldName);
		ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
		trustStoreType = Constraint.isNotNull(StringSupport.trimOrNull(fieldName),
				"trustStoreType field name cannot be null or empty.");
	}

	public void settrustStorePassword(@Nonnull @NotEmpty final String fieldName) {
		log.debug("{} Adding trustStorePassword to {}", getLogPrefix(), fieldName);
		ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
		trustStorePassword = Constraint.isNotNull(StringSupport.trimOrNull(fieldName),
				"trustStorePassword field name cannot be null or empty.");
	}
    
	/**
	 * Default constructor
	 */
	public AuthenticateMobile() {
		super();
	}

	@SuppressWarnings({ "unchecked" })
	protected boolean doPreExecute(@Nonnull final ProfileRequestContext profileRequestContext,
			@Nonnull final AuthenticationContext authenticationContext) {

		if (!super.doPreExecute(profileRequestContext, authenticationContext)) {
			return false;
		}

		if (authenticationContext.getAttemptedFlow() == null) {
			log.debug("{} No attempted flow within authentication context", getLogPrefix());
			ActionSupport.buildEvent(profileRequestContext, EventIds.INVALID_PROFILE_CTX);
			return false;
		}

		try {
			log.debug("{} Trying to create httpClient", getLogPrefix());
			httpClient = createHttpClient();

		} catch (Exception e) {
			log.debug("{} Error with doPreExecute. Cannot create httpClient", getLogPrefix(), e);
		}

		return true;
	}

	@Override
	protected void doExecute(@Nonnull final ProfileRequestContext profileRequestContext,
			@Nonnull final AuthenticationContext authenticationContext) {
		log.debug("{} Entering - doExecute", getLogPrefix());

		final MobileContext mobCtx = authenticationContext.getSubcontext(MobileContext.class, true);

		mobileNumber = StringSupport.trimOrNull(mobCtx.getMobileNumber());
		spamCode = StringSupport.trimOrNull(mobCtx.getSpamCode());

		/* TODO: Yes, yes. need to refactoring...just testing things */
		try {
			URIBuilder builder = new URIBuilder();
			builder.setScheme("https").setHost(authServer).setPort(authPort).setPath(authPath)
					.setParameter("mobileNumber", mobileNumber);
			if (spamCode != null) {
				builder.setParameter("spamCode", spamCode);
			}

			URI url = builder.build();

			final HttpGet httpGet = new HttpGet(url);
			Gson gson = new GsonBuilder().create();

			response = httpClient.execute(httpGet);
			log.debug("{} Response: {}", getLogPrefix(), response);

			int statusCode = response.getStatusLine().getStatusCode();
			log.debug("{} HTTPStatusCode {}", getLogPrefix(), statusCode);

			if (statusCode == HttpStatus.SC_OK) {

				HttpEntity entity = response.getEntity();
				String json = EntityUtils.toString(entity, "UTF-8");

				StatusResponse status = gson.fromJson(json, StatusResponse.class);

				log.debug("{} Gson commKey: {}", getLogPrefix(), status.getCommunicationDataKey());
				log.debug("{} Gson EventID: {}", getLogPrefix(), status.getEventId());
				log.debug("{} Gson ErrorMessage: {}", getLogPrefix(), status.getErrorMessage());
				response.close();

				mobCtx.setProcessState(ProcessState.IN_PROCESS);
				mobCtx.setConversationKey(status.getCommunicationDataKey());
				mobCtx.setEventId(status.getEventId());
				mobCtx.setErrorMessage(status.getErrorMessage());
			} else {
				mobCtx.setProcessState(ProcessState.ERROR);
				log.info("HttpClient Error: {}", response.getStatusLine().getStatusCode());
			}

		} catch (Exception e) {
			log.info("Exception: {}", e);
		}

	}

	private CloseableHttpClient createHttpClient() throws KeyStoreException {

		KeyStore trustStore = KeyStore.getInstance(trustStoreType);
		KeyStore clientStore = KeyStore.getInstance(keystoreType);

		try {
			trustStore.load(new FileInputStream(trustStorePath), trustStorePassword.toCharArray());
			clientStore.load(new FileInputStream(keystorePath), keystorePasswd.toCharArray());
		} catch (NoSuchAlgorithmException | CertificateException | IOException e1) {
			log.debug("Can't load key/trust stores", e1);
		}

		final SSLConnectionSocketFactory socketFactory;

		try {
			final SSLContext sslContext = SSLContexts.custom().useTLS()
					.loadKeyMaterial(clientStore, keystorePasswd.toCharArray()).loadTrustMaterial(trustStore).build();
			socketFactory = new SSLConnectionSocketFactory(sslContext,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		} catch (Exception e) {
			throw new RuntimeException("SSL initialization error", e);
		}
		final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
				.register("https", socketFactory).build();
		final BasicHttpClientConnectionManager connectionManager = new BasicHttpClientConnectionManager(registry);
		log.debug("Created httpClient");
		return HttpClients.custom().setConnectionManager(connectionManager).build();
	}

}
