package ee.lis.mock;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import ee.lis.interfaces.MyLabMessages.MyLabOrderMsg;
import ee.lis.interfaces.MyLabMessages.MyLabQueryMsg;
import ee.lis.interfaces.MyLabMessages.MyLabResultMsg;
import ee.lis.util.JsonUtil;
import java.util.concurrent.TimeUnit;
import org.junit.Assert;
import scala.concurrent.duration.FiniteDuration;

public class MockMyLab {
    private final ActorRef mockLIS;
    private final JavaTestKit httpProbe;

    public MockMyLab(ActorSystem system, String address, int port) {
        mockLIS = system.actorOf(Props.create(HttpServer.class));
        httpProbe = new JavaTestKit(system);
        mockLIS.tell(new HttpServerConf(address, port, httpProbe.getRef()), ActorRef.noSender());
    }

    public MockMyLab expect(MyLabResultMsg expectedResultMsg) {
        String actual = httpProbe.expectMsgClass(String.class);
        String expected = JsonUtil.asJson(expectedResultMsg);
        Assert.assertEquals(expected, actual);
        mockLIS.tell("", httpProbe.getRef());
        return this;
    }

    public MockMyLab expect(MyLabQueryMsg expectedQueryMsg) {
        String actual = httpProbe.expectMsgClass(String.class);
        String expected = JsonUtil.asJson(expectedQueryMsg);
        Assert.assertEquals(expected, actual);
        return this;
    }

    public MockMyLab send(MyLabOrderMsg orderMsg) {
        httpProbe.getLastSender().tell(JsonUtil.asJson(orderMsg), httpProbe.getRef());
        return this;
    }

    public MockMyLab send(String str) {
        httpProbe.getLastSender().tell(str, httpProbe.getRef());
        return this;
    }

    public String expectAnyString() {
        return httpProbe.expectMsgClass(FiniteDuration.apply(10, TimeUnit.SECONDS), String.class);
    }
}
