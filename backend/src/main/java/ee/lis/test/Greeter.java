package ee.lis.test;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import java.util.ArrayList;
import java.util.List;

public class Greeter extends AbstractActor {

    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef greeter = system.actorOf(Props.create(Greeter.class));
        greeter.tell(new Greet("Peeter"), ActorRef.noSender());
        greeter.tell(new SayFarewell("Peeter"), ActorRef.noSender());
        greeter.tell(new Stop(), ActorRef.noSender());
    }

    private final List<Greet> greetHistory = new ArrayList<>();

    private Greeter() {
        receive(ReceiveBuilder
            .match(Greet.class, msg -> {
                greetHistory.add(msg);
                System.out.println("Hello, " + msg.greetee);
            })
            .match(SayFarewell.class, msg -> {
                System.out.println("Goodbye, " + msg.farewellee);
            })
            .match(Stop.class, __ -> {
                System.out.println("Stopping");
                context().stop(self());
            })
            .build());
    }

    public static class Greet {
        public final String greetee;
        public Greet(String greetee) {
            this.greetee = greetee;
        }
    }
    public static class SayFarewell {
        public final String farewellee;
        public SayFarewell(String farewellee) {
            this.farewellee = farewellee;
        }
    }
    public static class Stop {
    }

}
