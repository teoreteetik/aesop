package ee.lis.mock;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import com.typesafe.config.Config;
import ee.lis.util.CommonProtocol.ConfMsg;
import java.util.concurrent.TimeUnit;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;

public class HttpServerConf implements ConfMsg {

    public enum ConfKey {
        address,
        port,
        recipientActor
    }

    public static HttpServerConf create(Config config, ActorContext ctx) {
        String address = config.getString(ConfKey.address.name());
        int portNumber = config.getInt(ConfKey.port.name());
        ActorRef recipientActor;
        Future<ActorRef> actorRefFuture = ctx.actorSelection(config.getString(ConfKey.recipientActor.name())).resolveOne(Duration.apply(1, TimeUnit.SECONDS));
        try {
            recipientActor = Await.result(actorRefFuture, FiniteDuration.apply(1, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new HttpServerConf(address, portNumber, recipientActor);
    }

    public final String address;
    public final int port;
    public final ActorRef recipientActor;

    public HttpServerConf(String address, int port, ActorRef recipientActor) {
        this.recipientActor = recipientActor;
        this.address = address;
        this.port = port;
    }
}
