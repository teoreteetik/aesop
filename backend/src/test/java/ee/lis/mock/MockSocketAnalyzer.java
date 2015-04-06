package ee.lis.mock;

import static ee.lis.TestUtils.convertLowLevelChars;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import ee.lis.flow_component.socket.SocketClient;
import ee.lis.flow_component.socket.SocketClientConf;
import ee.lis.flow_component.socket.SocketProtocol.BytesMessage;
import ee.lis.flow_component.socket.SocketServer;
import ee.lis.flow_component.socket.SocketServerConf;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import scala.concurrent.duration.FiniteDuration;

public class MockSocketAnalyzer {

    public static enum Mode {
        CLIENT,
        SERVER
    }

    private final ActorRef socketActor;
    private final JavaTestKit socketProbe;

    public MockSocketAnalyzer(ActorSystem system, String address, int port, Mode mode) {
        socketProbe = new JavaTestKit(system);

        if (mode == Mode.CLIENT) {
            socketActor = system.actorOf(Props.create(SocketClient.class), "mockAnalyzer");
            socketActor.tell(new SocketClientConf(address, port, socketProbe.getRef(), FiniteDuration.apply(5000, TimeUnit.MILLISECONDS)), ActorRef.noSender());
        } else {
            socketActor = system.actorOf(Props.create(SocketServer.class), "mockAnalyzer");
            socketActor.tell(new SocketServerConf(address,port, socketProbe.getRef()), ActorRef.noSender());
        }
    }

    public MockSocketAnalyzer send(String msgToSend) {
        socketActor.tell(new BytesMessage(convertLowLevelChars(msgToSend)), ActorRef.noSender());
        return this;
    }

    public MockSocketAnalyzer expect(String expectedMsg) {
        BytesMessage receivedMessage = socketProbe.expectMsgClass(BytesMessage.class);
        Assert.assertEquals(convertLowLevelChars(expectedMsg), new String(receivedMessage.getByteArray()));
        return this;
    }

    public MockSocketAnalyzer expectNoMsg() {
        socketProbe.expectNoMsg();
        return this;
    }
}