package fi.csc.shibboleth.mobileauth.impl.authn;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.net.ssl.SSLContext;

import org.apache.commons.collections.MapUtils;
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
import net.shibboleth.idp.authn.context.AuthenticationContext;
import net.shibboleth.idp.profile.AbstractProfileAction;
import net.shibboleth.idp.profile.ActionSupport;
import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.component.ComponentSupport;
import net.shibboleth.utilities.java.support.logic.Constraint;
import net.shibboleth.utilities.java.support.primitive.StringSupport;

/**
 * @author korteke
 *
 */
@SuppressWarnings("rawtypes")
public class ResolveAuthenticationStatus extends AbstractProfileAction {

    public static final String EVENTID_GATEWAY_ERROR = "GatewayError";

    /** Class logger. */
    @Nonnull
    private final Logger log = LoggerFactory.getLogger(ResolveAuthenticationStatus.class);

    @Nonnull
    private String authServer;

    @Nonnull
    private int authPort;

    @Nonnull
    private String statusPath;

    @Nonnull
    private String keystoreType;

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

    public void setstatusPath(@Nonnull @NotEmpty final String fieldName) {
        log.debug("{} Adding statusPath to {}", getLogPrefix(), fieldName);
        ComponentSupport.ifInitializedThrowUnmodifiabledComponentException(this);
        statusPath = Constraint.isNotNull(StringSupport.trimOrNull(fieldName),
                "statusPath field name cannot be null or empty.");
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

    public ResolveAuthenticationStatus() {
        super();
    }

    @Override
    protected boolean doPreExecute(ProfileRequestContext profileRequestContext) {
        log.debug("Entering ResolveAuthenticationStatus doPreExecute");

        if (profileRequestContext.getSubcontext(AuthenticationContext.class).getSubcontext(MobileContext.class,
                false) == null) {
            log.error("{} No mobile context in authentication context", getLogPrefix());
            ActionSupport.buildEvent(profileRequestContext, EventIds.INVALID_PROFILE_CTX);
            return false;
        }

        return true;

    }

    /** {@inheritDoc} */
    @Override
    protected void doExecute(@Nonnull final ProfileRequestContext profileRequestContext) {

        log.debug("Entering ResolveAuthenticationStatus doExecute");

        final MobileContext mobCtx = profileRequestContext.getSubcontext(AuthenticationContext.class)
                .getSubcontext(MobileContext.class, false);

        try {
            log.debug("Get authentication status for {} with convKey {}", mobCtx.getMobileNumber(),
                    mobCtx.getConversationKey());
            getStatusUpdate(mobCtx.getConversationKey(), profileRequestContext, mobCtx);
        } catch (Exception e) {
            log.debug("Failed to get authentication status", e);
        }
    }

    /**
     * Fetch status update from the backend
     * 
     * @param conversationKey
     * @param profileCtx
     * @param mobCtx
     */
    private void getStatusUpdate(String conversationKey, ProfileRequestContext profileCtx, MobileContext mobCtx) {
        log.debug("{} Getting statusUpdate for convKey {}", getLogPrefix(), conversationKey);

        final CloseableHttpClient httpClient;
        try {
            log.debug("{} Trying to create httpClient", getLogPrefix());
            httpClient = createHttpClient();
        } catch (KeyStoreException | RuntimeException e) {
            log.error("{} Cannot create httpClient", getLogPrefix(), e);
            ActionSupport.buildEvent(profileCtx, EventIds.RUNTIME_EXCEPTION);
            return;
        }

        HttpEntity entity = null;

        try {
            final URIBuilder builder = new URIBuilder();
            builder.setScheme("https").setHost(authServer).setPort(authPort).setPath(statusPath)
                    .setParameter("communicationDataKey", conversationKey);

            final URI url = builder.build();
            log.debug("{} getStatus URL: {}", getLogPrefix(), url.toURL());

            final HttpGet httpGet = new HttpGet(url);
            final Gson gson = new GsonBuilder().create();

            final CloseableHttpResponse response = httpClient.execute(httpGet);
            log.debug("{} Response: {}", getLogPrefix(), response);

            int statusCode = response.getStatusLine().getStatusCode();
            log.debug("{} HTTPStatusCode {}", getLogPrefix(), statusCode);

            if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
                log.error("{} Authentication failed for {} with error [{}]", getLogPrefix(), mobCtx.getMobileNumber(),
                        statusCode);
                mobCtx.setProcessState(ProcessState.ERROR);
                ActionSupport.buildEvent(profileCtx, EventIds.RUNTIME_EXCEPTION);
                return;
            }

            if (statusCode == HttpStatus.SC_OK) {

                entity = response.getEntity();
                final String json = EntityUtils.toString(entity, "UTF-8");

                final StatusResponse status = gson.fromJson(json, StatusResponse.class);

                log.debug("{} Gson commKey: {}", getLogPrefix(), status.getCommunicationDataKey());
                log.debug("{} Gson EventID: {}", getLogPrefix(), status.getEventId());
                log.debug("{} Gson ErrorMessage: {}", getLogPrefix(), status.getErrorMessage());

                response.close();

                final Map<String, String> attributes = status.getAttributes();

                if (status.getErrorMessage() == null && !MapUtils.isEmpty(attributes)) {

                    log.info("{} Authentication completed for {}", getLogPrefix(), mobCtx.getMobileNumber());
                    mobCtx.setProcessState(ProcessState.COMPLETE);
                    mobCtx.setAttributes(status.getAttributes());

                    if (log.isDebugEnabled()) {
                        for (Map.Entry<String, String> attr : status.getAttributes().entrySet()) {
                            log.debug("Attr: {} - Value: {}", attr.getKey(), attr.getValue());
                        }
                    }

                } else if (status.getErrorMessage() != null) {
                    log.info("{} Authentication failed for {} with error [{}]", getLogPrefix(),
                            mobCtx.getMobileNumber(), status.getErrorMessage());
                    mobCtx.setProcessState(ProcessState.ERROR);
                    mobCtx.setErrorMessage(status.getErrorMessage());
                } else {
                    log.info("{} Authentication in process for {}", getLogPrefix(), mobCtx.getMobileNumber());
                    mobCtx.setProcessState(ProcessState.IN_PROCESS);
                }

            } else {
                mobCtx.setProcessState(ProcessState.ERROR);
                log.error("{} Unexpected status code {} from REST gateway", getLogPrefix(), statusCode);
                ActionSupport.buildEvent(profileCtx, EVENTID_GATEWAY_ERROR);
                return;
            }

        } catch (Exception e) {
            log.error("Exception: {}", e);
            ActionSupport.buildEvent(profileCtx, EventIds.RUNTIME_EXCEPTION);
            return;
        } finally {
            EntityUtils.consumeQuietly(entity);
        }

    }

    
    /**
     * This method will create CloseableHttpClient with client certificate authentication
     * 
     * @return CloseableHttpClient
     * @throws KeyStoreException
     * @throws RuntimeException
     */
    private CloseableHttpClient createHttpClient() throws KeyStoreException, RuntimeException {

        KeyStore trustStore = KeyStore.getInstance(trustStoreType);
        KeyStore clientStore = KeyStore.getInstance(keystoreType);

        try {
            trustStore.load(new FileInputStream(trustStorePath), trustStorePassword.toCharArray());
            clientStore.load(new FileInputStream(keystorePath), keystorePasswd.toCharArray());
        } catch (NoSuchAlgorithmException | CertificateException | IOException e) {
            throw new RuntimeException("Cannot load key/trust stores", e);
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
