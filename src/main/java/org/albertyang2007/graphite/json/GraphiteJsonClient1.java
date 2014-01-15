package org.albertyang2007.graphite.json;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
/*
 * Using Jersey Client
 */
public class GraphiteJsonClient1 {
    public String getJsonDataFromWeb() {
        String output = "[]";
        try {

            ClientConfig clientConfig = new DefaultClientConfig();

            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);

            Client client = Client.create(clientConfig);

            WebResource webResource = client
                    .resource("http://192.168.1.131:9090/render?from=-30minute&target=summarize(system.loadavg_1min,'30minute')&format=json&noCache=true");

            ClientResponse response = webResource.accept("application/json").type("application/json")
                    .post(ClientResponse.class, new String());

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
            }

            output = response.getEntity(String.class);

            System.out.println("Server response .... \n");
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
        GraphiteJsonClient1 ins = new GraphiteJsonClient1();
        String jsonStr = ins.getJsonDataFromWeb();
        ins.deserializeJsonOutput(jsonStr);
    }
}
