package graphite_json_deserialize;

import static com.github.dreamhead.moco.Moco.httpserver;
import static com.github.dreamhead.moco.Runner.running;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.albertyang2007.graphite.json.GraphiteJsonDeserializeSample2;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.Runnable;

/*
 * using moco
 * refer to:http://www.blogbus.com/dreamhead-logs/225920824.html
 * https://github.com/dreamhead/moco
 */
public class GraphiteJsonClient3Test {
	String json1 = "{\"target\": \"system.loadavg.1min\", \"datapoints\": [[89, 1389163650], [45, 1389163660]]}";
	String json2 = "{\"target\": \"system.loadavg.15min\", \"datapoints\": [[null, 1389163650], [null, 1389163660]]}";

	String json = "[" + json1 + ", " + json2 + "]";

	@Test
	public void simple_test_using_httpclient() throws Exception {
		HttpServer server = httpserver(9090);
		server.response(json);

		running(server, new Runnable() {
			public void run() throws IOException {
				Content content = Request.Get("http://localhost:9090")
						.execute().returnContent();
				assertThat(content.asString(), is(json));

				ObjectMapper OBJECT_MAPPER = new ObjectMapper();
				List<GraphiteJsonDeserializeSample2> vos = OBJECT_MAPPER.readValue(
						content.asString(),
						new TypeReference<List<GraphiteJsonDeserializeSample2>>() {/**/
						});

				assertThat(vos.size(), is(2));
				assertThat(vos.get(0).getTarget(), is("system.loadavg.1min"));
				assertThat(vos.get(1).getTarget(), is("system.loadavg.15min"));
				assertThat(vos.get(0).getDatapoints().size(), is(2));
				assertThat(vos.get(1).getDatapoints().size(), is(0));
				assertThat(vos.get(0).getDatapoints().get(0).getValue(),
						is("89"));
				assertThat(vos.get(0).getDatapoints().get(0).getTimestamp(),
						is("1389163650"));
			}
		});
	}

	@Test
	public void simple_test_using_cxf_client() throws Exception {
		HttpServer server = httpserver(9090);
		server.response(json);

		running(server, new Runnable() {
			public void run() throws IOException {
				String serverURL = "http://localhost:9090";
				String output = "[]";
				ObjectMapper objectMapper = new ObjectMapper();
				List<JacksonJsonProvider> providerList = new ArrayList<JacksonJsonProvider>();
				JacksonJsonProvider provider = new JacksonJsonProvider();
				provider.addUntouchable(Response.class);
				provider.setMapper(objectMapper);

				providerList.add(provider);

				WebClient client = WebClient.create(serverURL, providerList);
				client.accept(MediaType.APPLICATION_JSON)
						.type(MediaType.APPLICATION_JSON)
						.path("render")
						.query("format", "json")
						.query("noCache", "true")
						.query("from", "-30minute")
						.query("target",
								"summarize(system.loadavg_1min,'30minute')");

				output = client.get(String.class);

				assertThat(output, is(json));
				
				ObjectMapper OBJECT_MAPPER = new ObjectMapper();
				List<GraphiteJsonDeserializeSample2> vos = OBJECT_MAPPER
						.readValue(
								output,
								new TypeReference<List<GraphiteJsonDeserializeSample2>>() {/**/
								});

				assertThat(vos.size(), is(2));
				assertThat(vos.get(0).getTarget(), is("system.loadavg.1min"));
				assertThat(vos.get(1).getTarget(), is("system.loadavg.15min"));
				assertThat(vos.get(0).getDatapoints().size(), is(2));
				assertThat(vos.get(1).getDatapoints().size(), is(0));
				assertThat(vos.get(0).getDatapoints().get(0).getValue(),
						is("89"));
				assertThat(vos.get(0).getDatapoints().get(0).getTimestamp(),
						is("1389163650"));
			}
		});
	}
}
