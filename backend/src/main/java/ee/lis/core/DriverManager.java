package ee.lis.core;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.typesafe.config.Config;
import ee.lis.driver.DynamicDriver;
import ee.lis.util.ConfigUtil;
import java.util.Set;

public class DriverManager extends AbstractLoggingActor {

    public static Props props(Config config) {
        return Props.create(DriverManager.class, () -> new DriverManager(config));
    }
    public DriverManager(Config config) {
        Set<String> analyzerNames = ConfigUtil.getTopLevelEntries(config);
        for (String analyzerName : analyzerNames) {
            Config analyzerConfig = config.getConfig(analyzerName);
            ActorRef analyzerDriver = context().actorOf(Props.create(DynamicDriver.class), analyzerName);
            analyzerDriver.tell(analyzerConfig, self());
        }
        receive(ReceiveBuilder
            .match(Object.class, this::unhandled)
            .build());
    }
}