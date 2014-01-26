package org.albertyang2007.graphite.json;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

/*
 * Using Apache CXF 2.6.11 Client
 * Refer to: http://cxf.apache.org/docs/jax-rs-client-api.html#JAX-RSClientAPI-CXFWebClientAPI
 * Refer to: http://clq9761.iteye.com/blog/1765707
 * http://stackoverflow.com/questions/11434991/how-to-write-a-rest-client-based-on-cxf-in-tomee
 * https://svn.apache.org/repos/asf/cxf/trunk/systests/jaxrs/src/test/java/org/apache/cxf/systest/jaxrs/JAXRSClientServerResourceJacksonSpringProviderTest.java
 * http://svn.apache.org/repos/asf/cxf/trunk/maven-plugins/archetypes/cxf-jaxrs-service/src/main/resources/archetype-resources/src/test/java/HelloWorldIT.java
 */
public class GraphiteJsonClient3 {
    public String getJsonDataFromWeb() {
        String output = "[]";
        try {
            String serverURL = "http://10.178.255.114:8080";
            String path = "/render?from=-30minute&target=summarize(system.loadavg_1min,'30minute', 'sum', true)&format=json&noCache=true";

            final ObjectMapper objectMapper = new ObjectMapper();

            List<JacksonJsonProvider> providerList = new ArrayList<JacksonJsonProvider>();
            JacksonJsonProvider provider = new JacksonJsonProvider();
            provider.addUntouchable(Response.class);
            provider.setMapper(objectMapper);

            providerList.add(provider);

            String key = "ericsson.ece.exposure.sms.parlayx.{test,sendsms}.inbound";
            String alias = key.hashCode() + "";

            WebClient client = WebClient.create(serverURL, providerList);
            client.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).path("render")
                    .query("format", "json").query("noCache", "true").query("from", "-30minutes")
                    .query("target", "summarize(alias(" + key + ", '" + alias + "'),\"30minute\", \"sum\", true)");

            System.out.println(client.getCurrentURI());
            System.out.println("Server response .... \n");

            //(1)
            /*
             * Response response = client.get(); if
             * (response.getStatus() != 200) {
             * System.out.println("Failed : HTTP error code : " +
             * response.getStatus()); } else { //InputStream is =
             * response.getEntity(); }
             */

            //(2)
            //output = client.get(String.class);

            //(3)
            //List<GraphiteJsonDeserializeSample2> list = (List<GraphiteJsonDeserializeSample2>) client
            //		.getCollection(GraphiteJsonDeserializeSample2.class);
            //for (GraphiteJsonDeserializeSample2 dto : list) {
            //	System.out.println(dto);
            //}

            //(4)
            Response r = client.accept(MediaType.APPLICATION_JSON).get();
            if (r.getStatus() == Response.Status.OK.getStatusCode()) {
                output = IOUtils.toString((InputStream) r.getEntity());
            }
            System.out.println(output);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    public List<GraphiteJsonDeserializeSample2> deserializeJsonOutput(String jsonStr) {
        List<GraphiteJsonDeserializeSample2> vos = null;
        try {
            ObjectMapper mapper = new ObjectMapper();
            vos = mapper.readValue(jsonStr, new TypeReference<List<GraphiteJsonDeserializeSample2>>() {/**/
            });

            for (GraphiteJsonDeserializeSample2 avo : vos)
                System.out.println(avo);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return vos;
    }

    public static void main(String[] args) {
        GraphiteJsonClient3 ins = new GraphiteJsonClient3();
        String jsonStr = ins.getJsonDataFromWeb();
        ins.deserializeJsonOutput(jsonStr);
    }
}
