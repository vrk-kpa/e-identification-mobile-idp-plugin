package fi.csc.shibboleth.mobileauth.api.authn.context;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.messaging.context.BaseContext;

import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;

public class MobileContext extends BaseContext {
	
	public static enum ProcessState {
		IN_PROCESS,
		COMPLETE,
		ERROR,
		UNKNOWN
	}
	
	/** The users mobile phone number **/
	private String mobileNumber;
	
	/** The users spam prevention code **/
	@Nullable 
	private String spamCode;
	
	/** ProcessState */
	@Nonnull
	private ProcessState processState;
	
	/** ConversationKey from ETSI-server */
	@Nonnull
	private String conversationKey;
	
	/** EventID */
	@Nonnull
	private String eventId;

	/** Error Message */
	private String errorMessage;
	
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getEventId() {
		return eventId;
	}

	public void setEventId(String eventId) {
		this.eventId = eventId;
	}

	/* Attributes from authentication rest-api **/
	@Nonnull
	@NotEmpty 
	private Map<String, String> attributes;
	
	/** Constructor */
	public MobileContext() {
		attributes = new HashMap<String, String>();
	}

	/** Set mobile number */
	@Nonnull @NotEmpty public String getMobileNumber() {
		return mobileNumber;
	}

	/** Set mobile number */
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	/** Get Spam prevention code */
	public String getSpamCode() {
		return spamCode;
	}

	/** Set Spam prevention code */
	public void setSpamCode(String spamCode) {
		this.spamCode = spamCode;
	}

	/** Get attribute map */
	public Map<String, String> getAttributes() {
		return attributes;
	}

	/** Set attribute map */
	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	/** Get process state message **/
	public ProcessState getProcessState() {
		return processState;
	}
	
	/** Set process state message **/
	public void setProcessState(ProcessState processState) {
		this.processState = processState;
	}

	/** Get Conversation key. Used between shibboleth and ETSI backend */
	public String getConversationKey() {
		return conversationKey;
	}
	
	/** Set Conversation key. Used between shibboleth and ETSI backend */
	public void setConversationKey(String conversationKey) {
		this.conversationKey = conversationKey;
	}

}
