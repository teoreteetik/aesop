package ee.lis.core.observer.ws_endpoint;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;
import com.typesafe.config.Config;
import ee.lis.core.observer.ObserverProtocol;
import ee.lis.core.observer.ObserverProtocol.AnalyzerInitialized;
import ee.lis.core.observer.ObserverProtocol.LogLevel;
import ee.lis.core.observer.ObserverProtocol.MsgProcessed;
import ee.lis.core.observer.ws_endpoint.WebSocketMsgs.*;
import ee.lis.util.JsonUtil;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;

public class WebSocketServer extends AbstractActor {

    private enum ConfKey {
        address,
        port
    }

    public static Props props(Config config) {
        String address = config.getString(ConfKey.address.name());
        int port = config.getInt(ConfKey.port.name());
        return Props.create(WebSocketServer.class, () -> new WebSocketServer(address, port));
    }

    private final Server server;

    private WebSocketServer(String address, int port) {
        this.server = new Server(address, port);
        server.start();
        receive(
            ReceiveBuilder
                .match(MsgProcessed.class, this::convertAndSend)
                .match(ObserverProtocol.LogEvent.class, this::convertAndSend)
                .match(AnalyzerInitialized.class, this::convertAndSend)
                .build()
        );
    }

    private void convertAndSend(MsgProcessed msg) {
        String analyzerId = getAnalyzerId(msg.recipient.path().toString());
        String senderComponentId = msg.sender.path().toString();
        String recipientCompontentId = msg.recipient.path().toString();
        String msgBody = msg.msg.toString();
        String stackTrace = msg.exception == null ? null : getStackTrace(msg.exception);
        ProcessedMsgEvent event = new ProcessedMsgEvent(analyzerId, senderComponentId, recipientCompontentId, msgBody, msg.startTime, msg.endTime, stackTrace);
        server.sendToAll(new WebSocketMsg(EventType.ProcessedMsgEvent, event));
    }


    private void convertAndSend(ObserverProtocol.LogEvent logEvent) {
        LogLevel logLevel = logEvent.logLevel;
        String analyzerId = getAnalyzerId(logEvent.componentId);
        String componentId = logEvent.componentId;
        String msgBody = logEvent.msgBody;
        long time = logEvent.time;

        LogEvent event = new LogEvent(logLevel, analyzerId, componentId, msgBody, time);
        server.sendToAll(new WebSocketMsg(EventType.LogEvent, event));
    }


    private void convertAndSend(AnalyzerInitialized analyzerInitialized) {
        String analyzerId = analyzerInitialized.driver.path().toString();
        String analyzerName = analyzerInitialized.driver.path().name();
        Map<String, String> componentNameById = new LinkedHashMap<>();
        for (ActorRef ar : analyzerInitialized.components)
            componentNameById.put(ar.path().toString(), ar.path().name());
        AnalyzerInfo info = new AnalyzerInfo(analyzerId, analyzerName, componentNameById);
        server.sendToAll(new WebSocketMsg(EventType.AnalyzerInfo, info));
    }

    private String getStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    private String getAnalyzerId(String componentId) {
        int index = componentId.indexOf("/DriverManager/");
        int endIndex = componentId.indexOf('/', index + "/DriverManager/".length());
        return componentId.substring(0, endIndex);
    }

    private class Server extends org.java_websocket.server.WebSocketServer {
        private final List<String> msgHistory = new ArrayList<>();

        public Server(String address, int port) {
            super(new InetSocketAddress(address, port));
        }

        @Override
        public void onOpen(WebSocket conn, ClientHandshake handshake) {
            msgHistory.forEach(conn::send);
        }

        @Override
        public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        }

        @Override
        public void onMessage(WebSocket conn, String message) {
        }

        @Override
        public void onError(WebSocket conn, Exception ex) {
        }

        public void sendToAll(Object msg) {
            String msgText = JsonUtil.asJson(msg);
            msgHistory.add(msgText);
            for (WebSocket c : connections())
                c.send(msgText);
        }
    }
}