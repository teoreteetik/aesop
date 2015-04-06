package ee.lis;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;
import ee.lis.core.FlowComponent;
import ee.lis.util.CommonProtocol.DestinationConf;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import scala.PartialFunction;
import scala.concurrent.Await;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.runtime.BoxedUnit;

public class HttpServerActor extends FlowComponent<DestinationConf> {


    public static class ResultMessageHandler extends HttpServlet {

        private final ActorRef destination;
        private final ActorRef self;

        public ResultMessageHandler(ActorRef destination, ActorRef self) {
            this.destination = destination;
            this.self = self;
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            destination.tell(req.getReader().readLine(), self);
        }
    }

    public static class QueryMessageHandler extends HttpServlet {
        private final ActorRef destination;

        public QueryMessageHandler(ActorRef destination) {
            this.destination = destination;
        }

        @Override
        protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String queryMsg = req.getReader().readLine();
            Future<Object> orderMessageFuture = Patterns.ask(destination, queryMsg, 10000);
            try {
                String orderMsg = (String) Await.result(orderMessageFuture, Duration.apply(10, TimeUnit.SECONDS));
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write(orderMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void init() throws Exception {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8070); //todo
        connector.setHost("localhost");
        server.setConnectors(new Connector[]{connector});

        ServletHandler handler = new ServletHandler();

        ResultMessageHandler resultHandler = new ResultMessageHandler(conf.destination, self());
        handler.addServletWithMapping(new ServletHolder(resultHandler), "/result");

        QueryMessageHandler queryMessageHandler = new QueryMessageHandler(conf.destination);
        handler.addServletWithMapping(new ServletHolder(queryMessageHandler), "/query");

        server.setHandler(handler);

        server.start();
        server.join();
    }

    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder.match(String.class, s -> {
        }).build();
    }
}
