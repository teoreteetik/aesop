package ee.lis.mock;

import akka.actor.ActorRef;
import ee.lis.core.ConfMsg;

public class HttpServerConf implements ConfMsg {

    public enum ConfKey {
        address,
        port,
        recipientActor
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
