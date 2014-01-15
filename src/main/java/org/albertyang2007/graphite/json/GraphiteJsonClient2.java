package org.albertyang2007.graphite.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/*
 * Using Spring Restemplate
 */
public class GraphiteJsonClient2 {

    public String getJsonDataFromWeb() {
        String output = "[]";
        try {

            RestTemplate restTemplate = new RestTemplate();
            MappingJacksonHttpMessageConverter converter = new MappingJacksonHttpMessageConverter();
            
            List<MediaType> supportedMediaTypes = new ArrayList<MediaType>();
            supportedMediaTypes.add(new MediaType("application", "json"));
            converter.setSupportedMediaTypes(supportedMediaTypes);
            
            restTemplate.getMessageConverters().add(converter);

            String serverUrl = "http://192.168.1.131:9090/render?from=-30minute&target=summarize(system.loadavg_1min,'30minute')&format=json&noCache=true";

            output = restTemplate.getForObject(serverUrl, String.class);

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
        GraphiteJsonClient2 ins = new GraphiteJsonClient2();
        String jsonStr = ins.getJsonDataFromWeb();
        ins.deserializeJsonOutput(jsonStr);
    }

}
