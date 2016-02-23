package fi.csc.shibboleth.mobileauth.impl.authn;

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.execution.Event;

public enum Events {
    success,
    failure,
    proceed,    
    networkError,
    gatewayError;

    public Event event(final Object source) {
        return new Event(source, name());
    }

    public Event event(final Object source, final AttributeMap<Object> attributes) {
        return new Event(source, name(), attributes);
    }
}