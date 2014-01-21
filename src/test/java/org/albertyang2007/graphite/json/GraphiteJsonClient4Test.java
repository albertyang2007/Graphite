package org.albertyang2007.graphite.json;

import static com.github.dreamhead.moco.Moco.by;
import static com.github.dreamhead.moco.Moco.header;
import static com.github.dreamhead.moco.Moco.httpserver;
import static com.github.dreamhead.moco.Moco.pathResource;
import static com.github.dreamhead.moco.Moco.uri;
import static com.github.dreamhead.moco.Moco.with;
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

    MocoHttpServer server;

    @Before
    public void setup() {
        //start moco http server
        HttpServer httpServer = httpserver(9090);

        httpServer.request(by(uri("/render?"))).response(with(pathResource("mocoConfig.json")),
                header("Content-Type", MediaType.APPLICATION_JSON));

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
        client.accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON).path("render")
                .query("format", "json").query("noCache", "true").query("from", "-30minute")
                .query("target", "summarize(system.loadavg_1min,'30minute')");

        output = client.get(String.class);

        deserializeAndAssertContext(output);
        
        try{
            Thread.sleep(1000000);
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void deserializeAndAssertContext(String output) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        List<GraphiteJsonDeserializeSample2> vos = OBJECT_MAPPER.readValue(output,
                new TypeReference<List<GraphiteJsonDeserializeSample2>>() {/**/
                });

        assertThat(vos.size(), is(2));
        assertThat(vos.get(0).getTarget(), is("system.loadavg.1min"));
        assertThat(vos.get(1).getTarget(), is("system.loadavg.15min"));
        assertThat(vos.get(0).getDatapoints().size(), is(2));
        assertThat(vos.get(1).getDatapoints().size(), is(0));
        assertThat(vos.get(0).getDatapoints().get(0).getValue(), is("89"));
        assertThat(vos.get(0).getDatapoints().get(0).getTimestamp(), is("1389163650"));
    }

}
