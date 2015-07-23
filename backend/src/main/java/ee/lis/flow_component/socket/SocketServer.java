package ee.lis.flow_component.socket;

import akka.actor.ActorRef;
import akka.io.Tcp;
import akka.io.Tcp.*;
import akka.io.TcpMessage;
import akka.japi.pf.ReceiveBuilder;
import akka.util.ByteString;
import ee.lis.flow_component.socket.SocketProtocol.BytesMessage;
import ee.lis.core.FlowComponent;
import java.net.InetSocketAddress;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;


public class SocketServer extends FlowComponent<SocketServerConf>{
    @Override
    protected void init() throws Exception {
        final ActorRef tcp = Tcp.get(context().system()).manager();
        tcp.tell(TcpMessage.bind(self(), new InetSocketAddress(conf.address, conf.portNumber), 10), self());
    }

    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(Bound.class, msg -> {
                log.info("Server listening for connection on " + conf.address + ":" + conf.portNumber);
            })
            .match(CommandFailed.class, msg -> {
                throw new RuntimeException("Unable to start server on " + conf.address + ":" + conf.portNumber + " - " + msg.cmd().failureMessage());
            })
            .match(Connected.class, msg -> {
                sender().tell(TcpMessage.register(self()), self());
                context().become(getConnectedState(sender()));
                unstashAll();
            })
            .match(BytesMessage.class, msg -> {
                stash();
            })
            .build();
    }

    private PartialFunction<Object, BoxedUnit> getConnectedState(ActorRef connection) {
        return ReceiveBuilder
            .match(BytesMessage.class, msg ->
                connection.tell(TcpMessage.write(ByteString.fromArray(msg.getByteArray())), self()))
            .match(Received.class, msg ->
                conf.recipientActor.tell(new BytesMessage(msg.data().toArray()), self()))
            .match(CommandFailed.class, msg -> {
                log.error("Connection with client lost");
                context().unbecome();
            })
            .match(ConnectionClosed.class, msg -> {
                log.error("Connection closed");
                context().become(getBehaviour(), true);
            })
            .match(Connected.class, msg -> {
                sender().tell(TcpMessage.close(), self());
            })
            .build();
    }
}