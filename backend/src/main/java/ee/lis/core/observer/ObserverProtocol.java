package ee.lis.core.observer;

import akka.actor.ActorRef;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

public class ObserverProtocol {

    public enum LogLevel implements Serializable {
        INFO,
        DEBUG,
        WARNING,
        ERROR,
    }

    public static class LogEvent implements Serializable {
        public final LogLevel logLevel;
        public final String msgBody;
        public final String componentId;
        public final long time;

        public LogEvent(LogLevel logLevel, String msgBody, String componentId, long time) {
            this.logLevel = logLevel;
            this.msgBody = msgBody;
            this.componentId = componentId;
            this.time = time;
        }
    }

    public static class AnalyzerInitialized implements Serializable {
        public final ActorRef driver;
        public final List<ActorRef> components;

        public AnalyzerInitialized(ActorRef driver, List<ActorRef> components) {
            this.driver = driver;
            this.components = Collections.unmodifiableList(components);
        }
    }

    public static class MsgProcessed implements Serializable {
        public final ActorRef sender;
        public final ActorRef recipient;
        public final Object msg;
        public final long startTime;
        public final long endTime;
        public final Throwable exception;

        public MsgProcessed(ActorRef sender, ActorRef recipient, Object msg, long startTime, long endTime) {
            this(sender, recipient, msg, startTime, endTime, null);
        }

        public MsgProcessed(ActorRef sender, ActorRef recipient, Object msg, long startTime, long endTime, Throwable exception) {
            this.msg = msg;
            this.sender = sender;
            this.recipient = recipient;
            this.startTime = startTime;
            this.endTime = endTime;
            this.exception = exception;
        }
    }
}