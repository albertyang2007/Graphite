package org.albertyang2007.graphite.json;

import static com.github.dreamhead.moco.Moco.and;
import static com.github.dreamhead.moco.Moco.by;
import static com.github.dreamhead.moco.Moco.eq;
import static com.github.dreamhead.moco.Moco.httpserver;
import static com.github.dreamhead.moco.Moco.method;
import static com.github.dreamhead.moco.Moco.query;
import static com.github.dreamhead.moco.Moco.uri;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.github.dreamhead.moco.HttpServer;
import com.github.dreamhead.moco.internal.ActualHttpServer;
import com.github.dreamhead.moco.internal.MocoHttpServer;

@RunWith(value = MockitoJUnitRunner.class)
public class GraphiteJsonClient4Test {

	String json1 = "{\"target\": \"system.loadavg.1min\", \"datapoints\": [[89, 1389163650], [45, 1389163660]]}";
	String json2 = "{\"target\": \"system.loadavg.15min\", \"datapoints\": [[null, 1389163650], [null, 1389163660]]}";

	String json = "[" + json1 + ", " + json2 + "]";

	MocoHttpServer server;

	@Before
	public void setup() {
		// start moco http server
		HttpServer httpServer = httpserver(9090);

		httpServer
				.request(
						and(by(method("get")),
								by(uri("/render")),
								eq(query("target"),
										"summarize(system.loadavg_1min,'30minute',sum,true)"),
								eq(query("from"), "-30minutes"),
								eq(query("noCache"), "true"),
								eq(query("format"), "json"))).response(json);

		// httpServer.request(by(file("src/main/resources/graphiteClientRequest.json"))).response("haha");
		// .response(with(pathResource("graphiteClientResponse.json")),
		// header("Content-Type", MediaType.APPLICATION_JSON));

		server = new MocoHttpServer((ActualHttpServer) httpServer);
		server.start();
	}

	@After
	public void tearDown() {
		server.stop();
	}

	@Test
	public void testGraphiteClientQuery() throws Exception {
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
				.query("from", "-30minutes")
				.query("target",
						"summarize(system.loadavg_1min,'30minute',sum,true)");

		output = client.get(String.class);

		System.out.println(output);

		deserializeAndAssertContext(output);

	}

	public void deserializeAndAssertContext(String output)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper OBJECT_MAPPER = new ObjectMapper();
		List<GraphiteJsonDeserializeSample2> vos = OBJECT_MAPPER.readValue(
				output,
				new TypeReference<List<GraphiteJsonDeserializeSample2>>() {/**/
				});

		assertThat(vos.size(), is(2));
	}

}
