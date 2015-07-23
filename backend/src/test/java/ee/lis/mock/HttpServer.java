package ee.lis.mock;

import akka.actor.ActorRef;
import akka.japi.pf.ReceiveBuilder;
import akka.pattern.Patterns;
import ee.lis.core.FlowComponent;
import ee.lis.mock.HttpServerConf;
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

public class HttpServer extends FlowComponent<HttpServerConf> {

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
        connector.setPort(conf.port);
        connector.setHost(conf.address);
        server.setConnectors(new Connector[]{connector});

        ServletHandler handler = new ServletHandler();

        ResultMessageHandler resultHandler = new ResultMessageHandler(conf.recipientActor, self());
        handler.addServletWithMapping(new ServletHolder(resultHandler), "/result");

        QueryMessageHandler queryMessageHandler = new QueryMessageHandler(conf.recipientActor);
        handler.addServletWithMapping(new ServletHolder(queryMessageHandler), "/query");

        server.setHandler(handler);

        server.start();
        server.join();
    }

    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder.match(String.class, this::unhandled).build();
    }
}
