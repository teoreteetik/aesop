package ee.lis.flow_component.astme138195;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import com.typesafe.config.Config;
import ee.lis.util.CommonProtocol.ConfMsg;
import java.util.concurrent.TimeUnit;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class AstmE138195ActorConf implements ConfMsg {

    public final int maxFrameSize;
    public final ActorRef lowLevelRecipient;
    public final ActorRef highLevelRecipient;

    public AstmE138195ActorConf(int maxFrameSize, ActorRef lowLevelRecipient, ActorRef highLevelRecipient) {
        this.maxFrameSize = maxFrameSize;
        this.lowLevelRecipient = lowLevelRecipient;
        this.highLevelRecipient = highLevelRecipient;
    }

    @Override
    public String toString() {
        return "AstmE138195ActorConf{" +
            "maxFrameSize=" + maxFrameSize +
            ", lowLevelRecipient=" + lowLevelRecipient +
            ", highLevelRecipient=" + highLevelRecipient +
            '}';
    }

    public enum ConfKey {
        maxFrameSize,
        lowLevelRecipient,
        highLevelRecipient
    }

    public static AstmE138195ActorConf create(Config config, ActorContext ctx) {
        int maxFrameSize = config.getInt(ConfKey.maxFrameSize.name());
        ActorRef lowLevelRecipient;
        ActorRef highLevelRecipient;
        Future<ActorRef> lowLevelRecipientFuture = ctx.actorSelection(config.getString(ConfKey.lowLevelRecipient.name())).resolveOne(Duration.apply(1, TimeUnit.SECONDS));
        Future<ActorRef> highLevelRecipientFuture = ctx.actorSelection(config.getString(ConfKey.highLevelRecipient.name())).resolveOne(Duration.apply(1, TimeUnit.SECONDS));
        try {
            lowLevelRecipient = Await.result(lowLevelRecipientFuture, Duration.apply(1, TimeUnit.SECONDS));
            highLevelRecipient = Await.result(highLevelRecipientFuture, Duration.apply(1, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new AstmE138195ActorConf(maxFrameSize, lowLevelRecipient, highLevelRecipient);
    }
}