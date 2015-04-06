package ee.lis.flow_component.socket;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import com.typesafe.config.Config;
import ee.lis.util.CommonProtocol.ConfMsg;
import java.util.concurrent.TimeUnit;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

public class SocketServerConf implements ConfMsg {

    public final String address;
    public final int portNumber;
    public final ActorRef recipientActor;

    public SocketServerConf(String address, int portNumber, ActorRef recipientActor) {
        this.address = address;
        this.portNumber = portNumber;
        this.recipientActor = recipientActor;
    }

    @Override
    public String toString() {
        return "SocketServerConf{" +
            "address='" + address + '\'' +
            ", portNumber=" + portNumber +
            ", recipientActor=" + recipientActor +
            '}';
    }

    public enum ConfKey {
        address,
        port,
        recipientActor
    }

    public static SocketServerConf create(Config config, ActorContext ctx) {
        String address = config.getString(ConfKey.address.name());
        int portNumber = config.getInt(ConfKey.port.name());
        ActorRef recipientActor;
        Future<ActorRef> actorRefFuture = ctx.actorSelection(config.getString(ConfKey.recipientActor.name())).resolveOne(Duration.apply(1, TimeUnit.SECONDS));
        try {
            recipientActor = Await.result(actorRefFuture, FiniteDuration.apply(1, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new SocketServerConf(address, portNumber, recipientActor);
    }

}
