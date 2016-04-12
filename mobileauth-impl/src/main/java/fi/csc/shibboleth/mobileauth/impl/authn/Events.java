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

import org.springframework.webflow.core.collection.AttributeMap;
import org.springframework.webflow.execution.Event;

/**
 * Events that can be send to the flow
 * 
 * <li>{@link #success}</li>
 * <li>{@link #failure}</li>
 * <li>{@link #proceed}</li>
 * <li>{@link #networkError}</li>
 * <li>{@link #gatewayError}</li>
 * 
 * @author korteke
 *
 */
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