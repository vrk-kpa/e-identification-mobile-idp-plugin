package fi.csc.shibboleth.mobileauth.api.authn;

import java.io.Serializable;
import java.util.Map;

@SuppressWarnings("serial")
public class StatusResponse implements Serializable {

	public static final String ATTRIBUTE_ID_MSISDN = "MSISDN_Identifier";

	private String communicationDataKey;

	private String errorMessage;

	private String eventId;

	private Map<String, String> attributes;

	public StatusResponse() {
		this.communicationDataKey = null;
		this.errorMessage = null;
		this.eventId = null;
		this.attributes = null;
	}

	public String getCommunicationDataKey() {
		return communicationDataKey;
	}

	public void setCommunicationDataKey(String key) {
		this.communicationDataKey = key;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String message) {
		this.errorMessage = message;
	}

	public String getEventId() {
		return this.eventId;
	}

	public void setEventId(String id) {
		this.eventId = id;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

}