package ee.lis.core.observer.ws_endpoint;

import static java.util.Collections.unmodifiableMap;
import ee.lis.core.observer.ObserverProtocol.LogLevel;
import java.util.Map;

public interface WebSocketMsgs {

    enum EventType {
        AnalyzerInfo,
        ProcessedMsgEvent,
        LogEvent
    }

    class WebSocketMsg {
        public final EventType eventType;
        public final Object event;

        public WebSocketMsg(EventType eventType, Object event) {
            this.eventType = eventType;
            this.event = event;
        }
    }

    class AnalyzerInfo {
        public final String id;
        public final String name;
        public final Map<String, String> componentNameById;

        public AnalyzerInfo(String id,
                            String name,
                            Map<String, String> componentNameById) {
            this.id = id;
            this.name = name;
            this.componentNameById = unmodifiableMap(componentNameById);
        }
    }

    class ProcessedMsgEvent {
        public final String analyzerId;
        public final String senderComponentId;
        public final String recipientComponentId;
        public final String msgBody;
        public final long processingStartTime;
        public final long processingEndTime;
        public final String stackTrace;

        public ProcessedMsgEvent(String analyzerId,
                                 String senderComponentId,
                                 String recipientComponentId,
                                 String msgBody,
                                 long processingStartTime,
                                 long processingEndTime,
                                 String stackTrace) {
            this.analyzerId = analyzerId;
            this.senderComponentId = senderComponentId;
            this.recipientComponentId = recipientComponentId;
            this.msgBody = msgBody;
            this.processingStartTime = processingStartTime;
            this.processingEndTime = processingEndTime;
            this.stackTrace = stackTrace;
        }
    }

    class LogEvent {
        public final LogLevel logLevel;
        public final String analyzerId;
        public final String componentId;
        public final String msgBody;
        public final long time;

        public LogEvent(LogLevel logLevel,
                        String analyzerId,
                        String componentId,
                        String msgBody,
                        long time) {
            this.logLevel = logLevel;
            this.analyzerId = analyzerId;
            this.componentId = componentId;
            this.msgBody = msgBody;
            this.time = time;
        }
    }
}