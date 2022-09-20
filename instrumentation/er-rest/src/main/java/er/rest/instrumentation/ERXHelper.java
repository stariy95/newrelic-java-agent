package er.rest.instrumentation;

import com.newrelic.api.agent.NewRelic;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

/**
 * Helper class that loads aad provides info about disabled controllers
 */
public class ERXHelper {
    public static final Set<String> disabledPaths = Collections.newSetFromMap(new ConcurrentHashMap<>());

    public static final boolean traceErrors = false;

    static {
        try {
            String disabledPaths = NewRelic.getAgent().getConfig()
                    .getValue("errest.disabled_paths", "");
            NewRelic.getAgent().getLogger().log(Level.CONFIG, "Disabled paths from config: " + disabledPaths);
            String[] disabledPathsConfig = disabledPaths.split(",");
            for (String path : disabledPathsConfig) {
                ERXHelper.disabledPaths.add(path.trim().toLowerCase());
            }
        } catch (Throwable th) {
            NewRelic.getAgent().getLogger().log(Level.WARNING, th, "Failed to read disabled paths from config");
        }
    }
}
