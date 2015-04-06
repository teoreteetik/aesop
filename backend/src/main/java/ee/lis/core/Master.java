package ee.lis.core;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import ee.lis.core.observer.Observer;
import ee.lis.core.observer.ObserverProtocol.AnalyzerInitialized;
import ee.lis.core.observer.ObserverProtocol.LogEvent;
import ee.lis.core.observer.ObserverProtocol.MsgProcessed;

public class Master extends AbstractActor {

    private Master() {
        Config config = ConfigFactory.load();

        Config driverManagerConfig = config.getConfig("DriverManager");
        context().actorOf(DriverManager.props(driverManagerConfig), "DriverManager");

        Config observerConfig = config.getConfig("Observer");
        ActorRef obsever = context().actorOf(Observer.props(observerConfig), "Observer");

        context().system().eventStream().subscribe(obsever, MsgProcessed.class);
        context().system().eventStream().subscribe(obsever, LogEvent.class);
        context().system().eventStream().subscribe(obsever, AnalyzerInitialized.class);
        receive(
            ReceiveBuilder
                .match(Object.class, this::unhandled)
                .build()
        );
    }
}