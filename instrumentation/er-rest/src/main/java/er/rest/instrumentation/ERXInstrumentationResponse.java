package er.rest.instrumentation;

import com.newrelic.api.agent.ExtendedResponse;
import com.newrelic.api.agent.HeaderType;
import com.webobjects.appserver.WOResponse;

/**
 * Bridge class that maps {@link WOResponse} to the NewRelic {@link ExtendedResponse}
 * NOTE: this response is constructed without actual response available and will provide empty response if none is set
 */
public class ERXInstrumentationResponse extends ExtendedResponse {

    WOResponse response;

    public void setResponse(WOResponse response) {
        this.response = response;
    }

    @Override
    public long getContentLength() {
        if(response == null) {
            return 0;
        }
        return response.contentInputStreamLength();
    }

    @Override
    public HeaderType getHeaderType() {
        return HeaderType.HTTP;
    }

    @Override
    public void setHeader(String name, String value) {
        if(response == null) {
            return;
        }
        response.setHeader(name, value);
    }

    @Override
    public int getStatus() {
        if(response == null) {
            return 0;
        }
        return response.status();
    }

    @Override
    public String getContentType() {
        // TODO: do we need this method?
        return "";
    }

    @Override
    public String getStatusMessage() {
        // TODO: do we need this method?
        return "";
    }
}
