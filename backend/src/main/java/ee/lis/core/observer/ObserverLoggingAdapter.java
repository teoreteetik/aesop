package ee.lis.core.observer;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.Logging.InitializeLogger;
import akka.japi.pf.ReceiveBuilder;
import ee.lis.core.observer.ObserverProtocol.LogEvent;
import ee.lis.core.observer.ObserverProtocol.LogLevel;
import java.util.HashMap;
import java.util.Map;

public class ObserverLoggingAdapter extends AbstractActor {

    private final Map<Integer, LogLevel> logLevels = new HashMap<Integer, LogLevel>() {{
        put(Logging.ErrorLevel(), LogLevel.ERROR);
        put(Logging.WarningLevel(), LogLevel.WARNING);
        put(Logging.InfoLevel(), LogLevel.INFO);
        put(Logging.DebugLevel(), LogLevel.DEBUG);
    }};

    public ObserverLoggingAdapter() {
        receive(
            ReceiveBuilder
                .match(InitializeLogger.class, __ -> sender().tell(Logging.loggerInitialized(), self()))
                .match(Logging.LogEvent.class, event ->
                    context().system().eventStream().publish(
                        new LogEvent(
                            logLevels.get(event.level()),
                            event.message().toString(),
                            event.logSource(),
                            event.timestamp())))
                .build());
    }
}
