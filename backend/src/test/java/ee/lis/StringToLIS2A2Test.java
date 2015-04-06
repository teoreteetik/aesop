package ee.lis;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.TestActorRef;
import ee.lis.flow_component.lis2a2_to_string.StringToLIS2A2Converter;
import org.junit.Test;

public class StringToLIS2A2Test {

    @Test
    public void test() {
        ActorSystem system = ActorSystem.create("test");
        final Props props = Props.create(StringToLIS2A2Converter.class);
        final TestActorRef<StringToLIS2A2Converter> ref = TestActorRef.create(system, props, "test");
        final StringToLIS2A2Converter actor = ref.underlyingActor();
        
    }

}
