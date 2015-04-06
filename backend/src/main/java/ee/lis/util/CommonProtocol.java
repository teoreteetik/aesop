package ee.lis.util;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import com.typesafe.config.Config;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class CommonProtocol {

    public interface ConfMsg extends Serializable {
    }

    public static class DestinationConf implements ConfMsg {
        public final ActorRef destination;

        public DestinationConf(ActorRef destination) {
            this.destination = destination;
        }

        @Override
        public String toString() {
            return "DestinationConf{" +
                "destination=" + destination +
                '}';
        }

        public enum ConfKey {
            destination
        }

        public static DestinationConf create(Config config, ActorContext ctx) {
            try {
                Future<ActorRef> destinationFuture = ctx.actorSelection(config.getString(ConfKey.destination.name())).resolveOne(Duration.apply(1, TimeUnit.SECONDS));
                ActorRef destination = Await.result(destinationFuture, Duration.apply(1, TimeUnit.SECONDS));
                return new DestinationConf(destination);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}