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

    public static class RecipientConf implements ConfMsg {
        public final ActorRef recipient;

        public RecipientConf(ActorRef recipient) {
            this.recipient = recipient;
        }

        @Override
        public String toString() {
            return "RecipientConf{" +
                "recipient=" + recipient +
                '}';
        }

        public enum ConfKey {
            recipient
        }

        public static RecipientConf create(Config config, ActorContext ctx) {
            try {
                Future<ActorRef> destinationFuture = ctx.actorSelection(config.getString(ConfKey.recipient.name())).resolveOne(Duration.apply(1, TimeUnit.SECONDS));
                ActorRef destination = Await.result(destinationFuture, Duration.apply(1, TimeUnit.SECONDS));
                return new RecipientConf(destination);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

}