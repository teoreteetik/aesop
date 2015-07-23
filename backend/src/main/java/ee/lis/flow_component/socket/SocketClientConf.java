package ee.lis.flow_component.socket;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import com.typesafe.config.Config;
import ee.lis.core.ConfMsg;
import java.util.concurrent.TimeUnit;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

public class SocketClientConf implements ConfMsg {

    public final String address;
    public final int portNumber;
    public final ActorRef recipientActor;
    public final FiniteDuration reconnectInterval;

    public SocketClientConf(String address,
                            int portNumber,
                            ActorRef recipientActor,
                            FiniteDuration reconnectInterval) {
        this.address = address;
        this.portNumber = portNumber;
        this.recipientActor = recipientActor;
        this.reconnectInterval = reconnectInterval;
    }

    @Override
    public String toString() {
        return "SocketClientConf{" +
            "address='" + address + '\'' +
            ", portNumber=" + portNumber +
            ", recipientActor=" + recipientActor +
            ", reconnectInterval=" + reconnectInterval +
            '}';
    }

    public enum ConfKey {
        address,
        port,
        recipientActor,
        reconnectInterval
    }

    public static SocketClientConf create(Config config, ActorContext ctx) {
        String address = config.getString(ConfKey.address.name());
        int portNumber = config.getInt(ConfKey.port.name());
        FiniteDuration reconnectInterval = Duration.apply(config.getInt(ConfKey.reconnectInterval.name()), TimeUnit.MILLISECONDS);
        ActorRef recipientActor;
        Future<ActorRef> actorRefFuture =  ctx.actorSelection(config.getString(ConfKey.recipientActor.name())).resolveOne(Duration.apply(1, TimeUnit.SECONDS));
        try {
            recipientActor = Await.result(actorRefFuture, FiniteDuration.apply(1, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new SocketClientConf(address, portNumber, recipientActor, reconnectInterval);
    }
}