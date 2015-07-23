package ee.lis.core.observer;

import akka.actor.AbstractLoggingActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.typesafe.config.Config;
import ee.lis.core.observer.ObserverProtocol.AnalyzerInitialized;
import ee.lis.core.observer.ObserverProtocol.LogEvent;
import ee.lis.core.observer.ObserverProtocol.MsgProcessed;
import ee.lis.util.ConfigUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Observer extends AbstractLoggingActor {

    private enum EndPointType {
        WebSocketServer(ee.lis.core.observer.ws_endpoint.WebSocketServer.class,
                        ee.lis.core.observer.ws_endpoint.WebSocketServer::props);

        public final Class<?> klass;
        public final Function<Config, Props> propsFunction;
        EndPointType(Class<?> klass, Function<Config, Props> propsFunction) {
            this.klass = klass;
            this.propsFunction = propsFunction;
        }
    }

    private final List<ActorRef> endpoints = new ArrayList<>();

    public static Props props(Config config) {
        return Props.create(Observer.class, () -> new Observer(config));
    }


    public Observer(Config config) {
        Config endpointsConfig = config.getConfig("endpoints");
        for (String endpointName : ConfigUtil.getTopLevelEntries(endpointsConfig)) {
            EndPointType endPointType = EndPointType.valueOf(endpointName);
            ActorRef endpoint = context().actorOf(endPointType.propsFunction.apply(endpointsConfig.getConfig(endpointName)), endpointName);
            endpoints.add(endpoint);
        }
        receive(
            ReceiveBuilder
                .match(MsgProcessed.class, this::tellAllEndpoints)
                .match(LogEvent.class, this::tellAllEndpoints)
                .match(AnalyzerInitialized.class, this::tellAllEndpoints)
                .build());
    }
    private void tellAllEndpoints(Object msg) {
        endpoints.stream().forEach(endpoint -> endpoint.tell(msg, self()));
    }
}