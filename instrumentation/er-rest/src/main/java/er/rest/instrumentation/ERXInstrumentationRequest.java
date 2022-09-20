package er.rest.instrumentation;

import com.newrelic.api.agent.ExtendedRequest;
import com.newrelic.api.agent.HeaderType;
import com.webobjects.appserver.WORequest;

import java.util.Collections;
import java.util.Enumeration;

/**
 * Bridge class that maps {@link WORequest} to the NewRelic {@link ExtendedRequest}
 */
public class ERXInstrumentationRequest extends ExtendedRequest {

    private final WORequest request;

    public ERXInstrumentationRequest(WORequest request) {
        this.request = request;
    }

    @Override
    public String getMethod() {
        return request.method();
    }

    @Override
    public HeaderType getHeaderType() {
        return HeaderType.HTTP;
    }

    @Override
    public String getHeader(String name) {
        return request.headerForKey(name);
    }

    @Override
    public String getRequestURI() {
        return request.uri();
    }

    @Override
    public String getRemoteUser() {
        return request._remoteAddress();
    }

    @Override
    public String getCookieValue(String name) {
        return request.cookieValueForKey(name);
    }

    // TODO: unimplemented

    @Override
    public Enumeration<?> getParameterNames() {
        return Collections.emptyEnumeration();
    }

    @Override
    public String[] getParameterValues(String name) {
        return new String[0];
    }

    @Override
    public Object getAttribute(String name) {
        return null;
    }

}
