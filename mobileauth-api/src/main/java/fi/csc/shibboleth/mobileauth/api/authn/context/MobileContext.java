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

package fi.csc.shibboleth.mobileauth.api.authn.context;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.opensaml.messaging.context.BaseContext;

import net.shibboleth.utilities.java.support.annotation.constraint.NotEmpty;
import net.shibboleth.utilities.java.support.primitive.StringSupport;

/**
 * Basic context that holds user related attributes
 * 
 * @author korteke
 *
 */
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
	
	/** User language */
	@Nonnull
	private String lang;

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
		lang = "fi";
	}

	public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        lang = StringSupport.trimOrNull(lang);
        
        if (lang != null && lang.length() == 2) {
            this.lang = lang;    
        }        
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
