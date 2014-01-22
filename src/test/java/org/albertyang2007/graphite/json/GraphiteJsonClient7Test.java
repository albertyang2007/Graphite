package org.albertyang2007.graphite.json;

import static com.github.dreamhead.moco.runner.JsonRunner.newJsonRunnerWithStreams;
import static com.google.common.base.Optional.of;
import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.InputStream;
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

import com.github.dreamhead.moco.runner.JsonRunner;
import com.google.common.io.Resources;

@RunWith(value = MockitoJUnitRunner.class)
public class GraphiteJsonClient7Test {
    private JsonRunner runner;
    private int port = 9090;

    @Before
    public void setup() {
        runWithConfiguration("asterisk_key_request.json");
    }

    @After
    public void tearDown() {
        if (runner != null) {
            runner.stop();
        }
    }

    protected void runWithConfiguration(String... resourceNames) {
        try {
            List<InputStream> streams = newArrayList();
            for (String resourceName : resourceNames) {
                streams.add(Resources.getResource(resourceName).openStream());
            }
            runner = newJsonRunnerWithStreams(streams, of(port));
            runner.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
                .query("format", "json").query("noCache", "true").query("from", "-5minutes")
                .query("target", "summarize(system.loadavg.cpu.*,'5minutes','sum',true)");

        System.out.println(client.getCurrentURI());
        
        //Thread.sleep(1000000);
        
        output = client.get(String.class);

        System.out.println(output);

        deserializeAndAssertContext(output);

    }

    public void deserializeAndAssertContext(String output) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper OBJECT_MAPPER = new ObjectMapper();
        List<GraphiteJsonDeserializeSample2> vos = OBJECT_MAPPER.readValue(output,
                new TypeReference<List<GraphiteJsonDeserializeSample2>>() {/**/
                });

        assertThat(vos.size(), is(2));
    }
}
