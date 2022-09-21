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

    static {
        try {
            String disabledPaths = NewRelic.getAgent().getConfig()
                    .getValue("errest.disabled_paths", "");
            String[] disabledPathsConfig = disabledPaths.split(",");
            for (String path : disabledPathsConfig) {
                String trimmedPath = path.trim();
                NewRelic.getAgent().getLogger().log(Level.CONFIG, "Disabled path from config: {0}", trimmedPath);
                ERXHelper.disabledPaths.add(trimmedPath);
            }
        } catch (Throwable th) {
            NewRelic.getAgent().getLogger().log(Level.WARNING, th, "Failed to read disabled paths from config");
        }
    }
}
