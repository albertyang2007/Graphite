package org.albertyang2007.graphite.json;

import static com.github.dreamhead.moco.Moco.httpserver;

import com.github.dreamhead.moco.HttpServer;

public class Test {

    public void justT() {

        String json1 = "{\"target\": \"ericsson.ece.exposure.sms.parlayx.sendsms.inbound\", \"datapoints\": [[898.0, 1389163650], [900.0, 1389163660]]}";
        String json2 = "{\"target\": \"ericsson.ece.exposure.sms.parlayx.test.inbound\", \"datapoints\": [[3456.0, 1389163650], [3578.0, 1389163660]]}";

        String json = "[" + json1 + ", " + json2 + "]";

        HttpServer server = httpserver(12345);
        server.response(json);

    }

    public static void main(String[] args) {
        Test ins = new Test();
        ins.justT();
        try {
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
