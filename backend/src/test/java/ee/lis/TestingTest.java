package ee.lis;

import akka.actor.ActorSystem;
import akka.testkit.JavaTestKit;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Lembit Gerz (lembit.gerz@gmail.com)
 */
public class TestingTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }
    @AfterClass
    public static void tearDown() {
        JavaTestKit.shutdownActorSystem(system);
        system = null;
    }


    @Test
    public void test() {

        new JavaTestKit(system) {{

//            final Props props = Props.create(SequenceActor.class);
//            final ActorRef lowLevelActor = system.actorOf(props);
//
//
//            final JavaTestKit lowLevelMock = new JavaTestKit(system);
//            final JavaTestKit highLevelMock = new JavaTestKit(system);
//            final AstmE138195Conf conf = new AstmE138195Conf(lowLevelMock.getRef(), highLevelMock.getRef(), 240);
//            lowLevelActor.tell(conf, ActorRef.noSender());
//
//            lowLevelActor.tell(EnqMsg, getRef());
//            lowLevelMock.expectMsgEquals(AckMsg);



        }};

    }


}
