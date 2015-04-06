package ee.lis.flow_component.socket;

import akka.actor.ActorRef;
import akka.io.Tcp;
import akka.io.Tcp.CommandFailed;
import akka.io.Tcp.Connected;
import akka.io.Tcp.ConnectionClosed;
import akka.io.Tcp.Received;
import akka.io.TcpMessage;
import akka.japi.pf.ReceiveBuilder;
import akka.util.ByteString;
import ee.lis.flow_component.socket.SocketProtocol.BytesMessage;
import ee.lis.core.FlowComponent;
import java.io.Serializable;
import java.net.InetSocketAddress;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class SocketClient extends FlowComponent<SocketClientConf> {

    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(CommandFailed.class, cmdFailed -> {
                scheduleRestart();
            })
            .match(Connected.class, msg -> {
                sender().tell(TcpMessage.register(self()), self());
                context().become(getConnectedState(sender()));
                unstashAll();
            })
            .match(RestartMsg.class, __ -> {
                throw new RuntimeException("Restart needed");
            })
            .match(BytesMessage.class, __ -> stash())
            .build();
    }

    @Override
    protected void init() throws Exception {
        InetSocketAddress remote = new InetSocketAddress(conf.address, conf.portNumber);
        final ActorRef tcp = Tcp.get(context().system()).manager();
        tcp.tell(TcpMessage.connect(remote), self());
    }

    private PartialFunction<Object, BoxedUnit> getConnectedState(ActorRef connection) {
        return ReceiveBuilder
            .match(BytesMessage.class, msg ->
                connection.tell(TcpMessage.write(ByteString.fromArray(msg.getByteArray())), self()))
            .match(Received.class, msg ->
                conf.recipientActor.tell(new BytesMessage(msg.data().toArray()), self()))
            .match(CommandFailed.class, msg -> {
                scheduleRestart();
                context().unbecome();
            })
            .match(ConnectionClosed.class, msg -> {
                scheduleRestart();
                context().unbecome();
            })
            .build();
    }

    private void scheduleRestart() {
        context().system().scheduler().scheduleOnce(conf.reconnectInterval, self(), new RestartMsg(), context().dispatcher(), self());
    }

    private static class RestartMsg implements Serializable {
    }
}