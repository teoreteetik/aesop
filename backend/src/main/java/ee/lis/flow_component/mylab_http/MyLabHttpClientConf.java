package ee.lis.flow_component.mylab_http;

import akka.actor.ActorContext;
import akka.actor.ActorRef;
import com.typesafe.config.Config;
import ee.lis.util.CommonProtocol.ConfMsg;
import java.util.concurrent.TimeUnit;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;

public class MyLabHttpClientConf implements ConfMsg {

    public final String resultUrl;
    public final String queryUrl;
    public final ActorRef recipient;

    public MyLabHttpClientConf(String resultUrl, String queryUrl, ActorRef recipient) {
        this.resultUrl = resultUrl;
        this.queryUrl = queryUrl;
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "MyLabHttpClientConf{" +
            "resultUrl='" + resultUrl + '\'' +
            ", queryUrl='" + queryUrl + '\'' +
            ", recipient=" + recipient +
            '}';
    }

    public enum ConfKey {
        resultUrl,
        queryUrl,
        recipient,
    }

    public static MyLabHttpClientConf create(Config config, ActorContext ctx) {
        String resultUrl = config.getString(ConfKey.resultUrl.name());
        String queryUrl = config.getString(ConfKey.queryUrl.name());
        ActorRef destination;
        Future<ActorRef> destinationFuture = ctx.actorSelection(config.getString(ConfKey.recipient.name())).resolveOne(Duration.apply(1, TimeUnit.SECONDS));
        try {
            destination = Await.result(destinationFuture, Duration.apply(1, TimeUnit.SECONDS));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new MyLabHttpClientConf(resultUrl, queryUrl, destination);
    }
}