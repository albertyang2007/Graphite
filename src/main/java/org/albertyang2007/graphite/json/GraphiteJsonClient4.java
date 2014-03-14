package org.albertyang2007.graphite.json;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.TrustManager;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.albertyang2007.graphite.auth.FakeTrustManager;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.jaxrs.client.JAXRSClientFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.transport.http.HTTPConduit;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;

public class GraphiteJsonClient4 {
    public String getJsonDataFromWeb() {
        String output = "[]";
        try {
            URL url = new URL("https://user:password@localhost:8443");
            int port = (url.getPort() == -1) ? url.getDefaultPort() : url.getPort();

            final ObjectMapper objectMapper = new ObjectMapper();

            List<JacksonJsonProvider> providerList = new ArrayList<JacksonJsonProvider>();
            JacksonJsonProvider provider = new JacksonJsonProvider();
            provider.addUntouchable(Response.class);
            provider.setMapper(objectMapper);

            providerList.add(provider);

            String key = "system.loadavg_1min";
            String alias = key.hashCode() + "";

            JAXRSClientFactoryBean bean = new JAXRSClientFactoryBean();
            //Setting: host and port
            bean.setAddress(String.format("%s://%s:%s", url.getProtocol(), url.getHost(), port));
            bean.setProviders(Collections.singletonList(new JacksonJsonProvider()));
            //Setting: Authentication user and password
            if (url.getUserInfo() != null) {
                String[] array = url.getUserInfo().split(":");
                if (array.length == 2) {
                    //user and password
                    bean.setUsername(array[0]);
                    bean.setPassword(array[1]);
                } else if (array.length == 1) {
                    //only user is provided
                    bean.setUsername(array[0]);
                }
            }
            WebClient client = bean.createWebClient();

            //Authentication setting: trust host and client
            TLSClientParameters tlsParams = new TLSClientParameters();
            TrustManager[] trustAllCerts;

            trustAllCerts = new TrustManager[] { new FakeTrustManager() };
            tlsParams.setTrustManagers(trustAllCerts);
            tlsParams.setDisableCNCheck(true);
            HTTPConduit httpConduit = WebClient.getConfig(client).getHttpConduit();
            httpConduit.setTlsClientParameters(tlsParams);

            client.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).path("render")
                    .query("format", "json").query("noCache", "true").query("from", "-30minutes")
                    .query("target", "summarize(alias(" + key + ", '" + alias + "'),\"30minute\", \"sum\", true)");

            System.out.println(client.getCurrentURI());

            List<GraphiteJsonDeserializeSample1> list = new ArrayList<GraphiteJsonDeserializeSample1>();
            list.addAll(client.getCollection(GraphiteJsonDeserializeSample1.class));

            System.out.println("Server response .... \n");
            for (GraphiteJsonDeserializeSample1 vo : list) {
                System.out.println(vo);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public static void main(String[] args) {
        GraphiteJsonClient4 ins = new GraphiteJsonClient4();
        ins.getJsonDataFromWeb();
    }
}
