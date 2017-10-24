package pl.marian.training.elasticsearch.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/*
 * 1) Start ES locally on default port
 * 2) Create index "es-workshop-1" with 2 shards each having 1 replica
 * 3) Type "example1" with
 *      - field "name" of string type so that it can be full text searched.
 *      - field "counter" which is able to store 64-bit integer
 *      - field "active" storing just true or false.
 */
public class CreateIndexRestApiTest {

    private static final String ES_REST_API = "http://localhost:9200/";

    private ObjectMapper mapper = new ObjectMapper();

    private RestTemplate restTemplate = new RestTemplate();

    @Test
    public void clusterIsUpAndRunning() throws Exception {
        URI uri = UriComponentsBuilder.fromHttpUrl(ES_REST_API)
                .pathSegment("_cluster", "health")
                .build().toUri();

        Map<String, Object> responseMap = getAsMap(uri);

        assertThat(responseMap.get("cluster_name")).isEqualTo("elasticsearch");
        assertThat(responseMap.get("status")).isIn("yellow", "green");
    }

    @Test
    public void esWorkShop1IndexCreated() throws Exception {
        URI uri = UriComponentsBuilder.fromHttpUrl(ES_REST_API)
                .pathSegment("es-workshop-1", "_settings")
                .build().toUri();

        ResponseEntity<String> jsonResponse = getAsJsonString(uri);

        assertThat(jsonResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void esWorkShop1IndexHasProperShardsNumber() throws Exception {
        URI uri = UriComponentsBuilder.fromHttpUrl(ES_REST_API)
                .pathSegment("es-workshop-1", "_settings")
                .build().toUri();

        ResponseEntity<String> jsonResponse = getAsJsonString(uri);

        String shardsNumber = JsonPath.parse(jsonResponse.getBody())
                .read("$['es-workshop-1']['settings']['index']['number_of_shards']");
        assertThat(shardsNumber).isEqualTo("3");
    }

    @Test
    public void esWorkShop1IndexHasProperReplica() throws Exception {
        URI uri = UriComponentsBuilder.fromHttpUrl(ES_REST_API)
                .pathSegment("es-workshop-1", "_settings")
                .build().toUri();

        ResponseEntity<String> jsonResponse = getAsJsonString(uri);

        String replicaCount = JsonPath.parse(jsonResponse.getBody())
                .read("$['es-workshop-1']['settings']['index']['number_of_replicas']");
        assertThat(replicaCount).isEqualTo("1");
    }

    @Test
    public void esWorkShop1IndexHasMappingExample1() throws Exception {
        URI uri = UriComponentsBuilder.fromHttpUrl(ES_REST_API)
                .pathSegment("es-workshop-1", "_mappings")
                .build().toUri();

        ResponseEntity<String> jsonResponse = getAsJsonString(uri);

        Object mappingExample1 = JsonPath.parse(jsonResponse.getBody())
                .read("$['es-workshop-1']['mappings']['example1']");
        assertThat(mappingExample1).isNotNull();
    }

    @Test
    public void mappingExample1HasNameColumn() throws Exception {
        URI uri = UriComponentsBuilder.fromHttpUrl(ES_REST_API)
                .pathSegment("es-workshop-1", "_mappings")
                .build().toUri();

        ResponseEntity<String> jsonResponse = getAsJsonString(uri);

        Map<String, Object> nameMapping = JsonPath.parse(jsonResponse.getBody())
                .read("$['es-workshop-1']['mappings']['example1']['properties']['name']");
        assertThat(nameMapping).isNotNull();
        assertThat(nameMapping.get("type")).isEqualTo("text");
    }

    @Test
    public void mappingExample1HasCounterColumn() throws Exception {
        URI uri = UriComponentsBuilder.fromHttpUrl(ES_REST_API)
                .pathSegment("es-workshop-1", "_mappings")
                .build().toUri();

        ResponseEntity<String> jsonResponse = getAsJsonString(uri);

        Map<String, Object> nameMapping = JsonPath.parse(jsonResponse.getBody())
                .read("$['es-workshop-1']['mappings']['example1']['properties']['counter']");
        assertThat(nameMapping).isNotNull();
        assertThat(nameMapping.get("type")).isEqualTo("long");
    }

    @Test
    public void mappingExample1HasActiveColumn() throws Exception {
        URI uri = UriComponentsBuilder.fromHttpUrl(ES_REST_API)
                .pathSegment("es-workshop-1", "_mappings")
                .build().toUri();

        ResponseEntity<String> jsonResponse = getAsJsonString(uri);

        Map<String, Object> nameMapping = JsonPath.parse(jsonResponse.getBody())
                .read("$['es-workshop-1']['mappings']['example1']['properties']['active']");
        assertThat(nameMapping).isNotNull();
        assertThat(nameMapping.get("type")).isEqualTo("boolean");
    }

    private Map<String, Object> getAsMap(URI uri) throws IOException {
        ResponseEntity<String> response = getAsJsonString(uri);
        return mapper.readValue(response.getBody(), new TypeReference<Map<String, Object>>() {
        });
    }

    private ResponseEntity<String> getAsJsonString(URI uri) {
        return restTemplate.getForEntity(uri, String.class);
    }
}
