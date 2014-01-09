package org.albertyang2007.graphite.json;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class GraphiteJsonDeserializeSample1 {
    private String target;
    private List<List<String>> datapoints;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public List<List<String>> getDatapoints() {
        return datapoints;
    }

    public void setDatapoints(List<List<String>> datapoints) {
        this.datapoints = datapoints;
    }

    @Override
    public String toString() {
        StringBuffer strbuf = new StringBuffer();
        strbuf.append("{\"target\": \"");
        strbuf.append(target);
        strbuf.append("\", \"datapoints\": ");

        strbuf.append(datapoints);

        strbuf.append("}");

        return strbuf.toString();
    }
    
    public static void main(String[] args){
        String json1 = "{\"target\": \"system.loadavg_1min\", \"datapoints\": [[89, 1389163650], [45, 1389163660]]}";
        String json2 = "{\"target\": \"system.loadavg_1min\", \"datapoints\": [[null, 1389163650], [null, 1389163660]]}";

        String json = "[" + json1 + ", " + json2 + "]";

        try {
            ObjectMapper OBJECT_MAPPER = new ObjectMapper();
            List<GraphiteJsonDeserializeSample1> vos = OBJECT_MAPPER.readValue(json,
                    new TypeReference<List<GraphiteJsonDeserializeSample1>>() {
                    });

            for (GraphiteJsonDeserializeSample1 avo : vos)
                System.out.println(avo);

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
