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

public class GraphiteJsonDataPointVODeserializer extends
		JsonDeserializer<List<GraphiteJsonDataPointVO>> {

	@Override
	public List<GraphiteJsonDataPointVO> deserialize(JsonParser jsonParser,
			DeserializationContext ctxt) {

		List<GraphiteJsonDataPointVO> datapoints = new ArrayList<GraphiteJsonDataPointVO>();
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			ObjectCodec oc = jsonParser.getCodec();
			JsonNode node = oc.readTree(jsonParser);

			List<List<String>> dpsL = objectMapper.readValue(node.traverse(),
					new TypeReference<List<List<String>>>() {
					});

			for (List<String> dps : dpsL) {
				if (dps.size() == 2 && dps.get(0) != null) {
					GraphiteJsonDataPointVO vo = new GraphiteJsonDataPointVO();
					vo.setValue(dps.get(0));
					vo.setTimestamp(dps.get(1));
					datapoints.add(vo);
				}
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
