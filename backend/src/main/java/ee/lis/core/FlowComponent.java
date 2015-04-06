package ee.lis.core;

import akka.actor.AbstractActorWithStash;
import akka.event.EventStream;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.ReceiveBuilder;
import ee.lis.core.observer.ObserverProtocol.MsgProcessed;
import ee.lis.util.CommonProtocol.ConfMsg;
import java.util.Collections;
import java.util.Set;
import scala.Option;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;


public abstract class FlowComponent<T extends ConfMsg> extends AbstractActorWithStash {

    protected final LoggingAdapter log;
    protected T conf;
    private EventStream eventStream;

    protected abstract PartialFunction<Object, BoxedUnit> getBehaviour();

    @SuppressWarnings("unchecked")
    protected FlowComponent() {
        this.eventStream = context().system().eventStream();
        log = Logging.getLogger(context().system(), this);

        receive(ReceiveBuilder
            .match(ConfMsg.class, conf -> {
                this.conf = (T) conf;
                init();
                context().become(getBehaviour());
                unstashAll();
            })
            .matchAny(__ -> stash())
            .build());
    }

    @Override
    public void aroundReceive(PartialFunction<Object, BoxedUnit> receive, Object msg) {
        long start = System.currentTimeMillis();
        if (getUnloggedTypes().contains(msg.getClass())) {
            super.aroundReceive(receive, msg);
        } else {
            try {
                super.aroundReceive(receive, msg);
                eventStream.publish(new MsgProcessed(sender(), self(), msg, start, System.currentTimeMillis()));
            } catch (Throwable t) {
                eventStream.publish(new MsgProcessed(sender(), self(), msg, start, System.currentTimeMillis(), t));
                throw t;
            }
        }
    }

    public Set<Class<?>> getUnloggedTypes() {
        return Collections.emptySet();
    }

    protected void init() throws Exception {
    }

    @Override
    public void preRestart(Throwable reason, Option<Object> message) {
        super.preRestart(reason, message);
        self().tell(conf, self());
    }
}