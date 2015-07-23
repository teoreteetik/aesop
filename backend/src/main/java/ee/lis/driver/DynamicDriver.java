package ee.lis.driver;

import static akka.actor.SupervisorStrategy.*;
import static java.util.stream.Collectors.toList;
import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.japi.pf.ReceiveBuilder;
import com.typesafe.config.Config;
import ee.lis.core.ConfMsg;
import ee.lis.core.observer.ObserverProtocol.AnalyzerInitialized;
import ee.lis.util.ConfigUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import scala.concurrent.duration.FiniteDuration;

public class DynamicDriver extends AbstractActor {

    private final List<Component> components = new ArrayList<>();
    private final SupervisorStrategy supervisorStrategy;

    private DynamicDriver() {
        supervisorStrategy = new OneForOneStrategy(20, FiniteDuration.apply(1, TimeUnit.MINUTES), DeciderBuilder.
            match(ActorInitializationException.class, e -> escalate()).
            match(ActorKilledException.class, e -> stop()).
            match(Exception.class, e -> restart()).
            matchAny(__ -> escalate()).build());

        receive(
            ReceiveBuilder
                .match(Config.class, config -> {
                    for (String componentName : ConfigUtil.getTopLevelEntries(config)) {
                        String componentTypeName = config.hasPath("type") ? config.getString("type") : componentName;
                        ComponentType componentType = ComponentType.valueOf(componentTypeName);
                        ActorRef componentActor = context().actorOf(Props.create(componentType.klass), componentName);
                        components.add(new Component(componentName, componentActor, componentType.configFuction));
                    }
                    context().system().eventStream().publish(new AnalyzerInitialized(self(), components.stream().map(c -> c.actorRef).collect(toList())));
                    for (Component component : components) {
                        ConfMsg componentConfig = component.confFunction.apply(config.getConfig(component.name), context());
                        component.actorRef.tell(componentConfig, self());
                    }
                })
                .build()
        );
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return supervisorStrategy;
    }

    private static class Component {
        public final String name;
        public final ActorRef actorRef;
        public final BiFunction<Config, ActorContext, ConfMsg> confFunction;

        private Component(String name, ActorRef actorRef, BiFunction<Config, ActorContext, ConfMsg> confFunction) {
            this.name = name;
            this.actorRef = actorRef;
            this.confFunction = confFunction;
        }
    }
}