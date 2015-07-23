package ee.lis.core;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class Main {
    public Main() {
        ActorSystem system = ActorSystem.create("Main");
        system.actorOf(Props.create(Master.class), "Master");
    }

    public static void main(String[] args) {
        new Main();
    }
}