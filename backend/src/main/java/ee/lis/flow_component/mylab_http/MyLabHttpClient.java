package ee.lis.flow_component.mylab_http;

import akka.japi.pf.ReceiveBuilder;
import ee.lis.mylab_interface.MyLabMessages.MyLabOrderMsg;
import ee.lis.mylab_interface.MyLabMessages.MyLabQueryMsg;
import ee.lis.mylab_interface.MyLabMessages.MyLabResultMsg;
import ee.lis.core.FlowComponent;
import ee.lis.util.JsonUtil;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;

public class MyLabHttpClient extends FlowComponent<MyLabHttpClientConf> {

    @Override
    protected PartialFunction<Object, BoxedUnit> getBehaviour() {
        return ReceiveBuilder
            .match(MyLabResultMsg.class, this::sendResult)
            .match(MyLabQueryMsg.class, this::sendQuery)
            .build();
    }

    private void sendResult(MyLabResultMsg resultMsg) throws IOException {
        executePOST(JsonUtil.asJson(resultMsg), conf.resultUrl);
    }

    private void sendQuery(MyLabQueryMsg queryMsg) throws IOException {
        String queryResponse = executePOST(JsonUtil.asJson(queryMsg), conf.queryUrl);
        MyLabOrderMsg orderMsg = JsonUtil.fromJson(queryResponse, MyLabOrderMsg.class);
        conf.destination.tell(orderMsg, self());
    }

    private String executePOST(String requestBody, String targetUrl) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(targetUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            DataOutputStream outStream = new DataOutputStream(connection.getOutputStream());
            outStream.writeBytes(requestBody);
            outStream.flush();
            outStream.close();

            InputStream inStream = connection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
            return br.lines().collect(Collectors.joining());
        } finally {
            if (connection != null)
                connection.disconnect();
        }
    }
}