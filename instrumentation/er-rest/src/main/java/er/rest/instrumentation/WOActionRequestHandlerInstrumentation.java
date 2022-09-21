package er.rest.instrumentation;

import com.newrelic.agent.bridge.AgentBridge;
import com.newrelic.agent.bridge.Transaction;
import com.newrelic.agent.bridge.TransactionNamePriority;
import com.newrelic.api.agent.weaver.MatchType;
import com.newrelic.api.agent.weaver.Weave;
import com.newrelic.api.agent.weaver.Weaver;
import com.webobjects.appserver.WORequest;
import com.webobjects.appserver.WOResponse;

import java.util.logging.Level;

/**
 * Instrumentation for the generic WO Action.
 * NOTE: this instrumentation will affect not only ERRest controllers but also any UI-related WO actions.
 */
@Weave(type = MatchType.ExactClass, originalName = "com.webobjects.appserver._private.WOActionRequestHandler")
public class WOActionRequestHandlerInstrumentation {

    @SuppressWarnings("unused")
    public WOResponse handleRequest(WORequest request) {
        // check if this call should be ignored
        boolean disabledTracing = ERXHelper.disabledPaths.contains(request.requestHandlerPath());
        Transaction tx = null;
        ERXInstrumentationResponse instrumentationResponse = null;

        if(disabledTracing) {
            // ignore transaction if it's started by someone else
            AgentBridge.getAgent().getTransaction(false).ignore();
        } else {
            try {
                // start transaction
                tx = AgentBridge.getAgent().getTransaction(true);
                if (tx != null) {
                    instrumentationResponse = new ERXInstrumentationResponse();
                    tx.setTransactionName(TransactionNamePriority.FRAMEWORK, false, "ERRest", request.requestHandlerPath());
                    // mark this transaction as a web one providing request and response details
                    tx.requestInitialized(new ERXInstrumentationRequest(request), instrumentationResponse);
                }
            } catch (Throwable th) {
                AgentBridge.getAgent().getLogger().log(Level.WARNING, th, "Unable to start ERRest transaction");
            }
        }

        // Here goes original method call
        WOResponse response = Weaver.callOriginal();
        if(!disabledTracing && tx != null) {
            // add response details
            if(instrumentationResponse != null) {
                instrumentationResponse.setResponse(response);
            }
            // mark transaction end
            tx.requestDestroyed();
        }
        return response;
    }

//    this method is abstract here, but defined in WODirectActionRequestHandler
//    @SuppressWarnings("unused")
//    public WOResponse generateErrorResponse(Exception exception, WOContext aContext) {
//        // report exception if this controller is not disabled
//        boolean disabledTracing = aContext.request() != null
//                && ERXHelper.disabledPaths.contains(aContext.request().requestHandlerPath());
//        if(!disabledTracing) {
//            NewRelic.noticeError(exception);
//        }
//        return Weaver.callOriginal();
//    }
}
