package org.albertyang2007.graphite.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.ObjectCodec;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

public class GraphiteJsonDataPointVODeserializer3 extends JsonDeserializer<List<GraphiteJsonDataPointVO3>> {

    @Override
    public List<GraphiteJsonDataPointVO3> deserialize(JsonParser jsonParser, DeserializationContext ctxt) {

        List<GraphiteJsonDataPointVO3> datapoints = new ArrayList<GraphiteJsonDataPointVO3>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectCodec oc = jsonParser.getCodec();
            JsonNode node = oc.readTree(jsonParser);

            List<List<String>> dpsL = objectMapper.readValue(node.traverse(), new TypeReference<List<List<String>>>() {
            });

            for (List<String> lis : dpsL) {
                GraphiteJsonDataPointVO3 vo3 = new GraphiteJsonDataPointVO3(lis);
                datapoints.add(vo3);
            }

        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return datapoints;
    }

}
