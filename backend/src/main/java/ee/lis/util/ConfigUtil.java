package ee.lis.util;

import com.typesafe.config.Config;
import java.util.Set;
import java.util.stream.Collectors;

public class ConfigUtil {
    public static Set<String> getTopLevelEntries(Config config) {
        return config.entrySet().stream()
            .map(entry -> entry.getKey().split("\\.", 2)[0])
            .collect(Collectors.toSet());
    }
}