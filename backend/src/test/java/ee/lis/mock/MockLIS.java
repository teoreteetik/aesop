package ee.lis.mock;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.JavaTestKit;
import ee.lis.HttpServerActor;
import ee.lis.util.CommonProtocol.DestinationConf;
import ee.lis.mylab_interface.MyLabMessages.MyLabOrderMsg;
import ee.lis.mylab_interface.MyLabMessages.MyLabQueryMsg;
import ee.lis.mylab_interface.MyLabMessages.MyLabResultMsg;
import ee.lis.util.JsonUtil;
import org.junit.Assert;

/**
 * @author Lembit Gerz (lembit.gerz@gmail.com)
 */
public class MockLIS {

    private final ActorRef mockLIS;
    private final JavaTestKit httpProbe;

    public MockLIS(ActorSystem system, int port) {
        mockLIS = system.actorOf(Props.create(HttpServerActor.class), "mockLIS");
        httpProbe = new JavaTestKit(system);
        mockLIS.tell(new DestinationConf(httpProbe.getRef()), ActorRef.noSender());
    }

    public MockLIS expect(MyLabResultMsg expectedResultMsg) {
        String actual = httpProbe.expectMsgClass(String.class);
        String expected = JsonUtil.asJson(expectedResultMsg);
        Assert.assertEquals(expected, actual);
        mockLIS.tell("", httpProbe.getRef());
        return this;
    }

    public MockLIS expect(MyLabQueryMsg expectedQueryMsg) {
        String actual = httpProbe.expectMsgClass(String.class);
        String expected = JsonUtil.asJson(expectedQueryMsg);
        Assert.assertEquals(expected, actual);
        return this;
    }

    public MockLIS send(MyLabOrderMsg orderMsg) {
        httpProbe.getLastSender().tell(JsonUtil.asJson(orderMsg), httpProbe.getRef());
        return this;
    }

    public MockLIS send(String str) {
                httpProbe.getLastSender().tell(str, httpProbe.getRef());
        return this;
    }
}
