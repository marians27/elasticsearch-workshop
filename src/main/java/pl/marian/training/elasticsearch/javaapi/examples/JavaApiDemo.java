package pl.marian.training.elasticsearch.javaapi.examples;

import pl.marian.training.elasticsearch.javaapi.TransportClientExecutor;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static pl.marian.training.elasticsearch.javaapi.examples.JavaApiExamplesConfig.INDEX_NAME;
import static pl.marian.training.elasticsearch.javaapi.examples.JavaApiExamplesConfig.TYPE_NAME;
import static java.util.Arrays.asList;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class JavaApiDemo {

    private static final List<String> MESSAGES = asList("Simple API", "Get API test", "ElasticSearch test",
            "Transport Client example", "Java client example");
    private static final int COUNT = 100;

    public static void main(String[] args) throws Exception {
        TransportClientExecutor executor = new TransportClientExecutor();
        executor.execute(JavaApiDemo::cleanupIndex);
        executor.execute(JavaApiDemo::populateData);
        executor.execute(JavaApiDemo::getApiExample);
        System.out.println("****************** SEARCH TERM *********************");
        executor.execute(JavaApiDemo::searchApiTermExample);
        System.out.println("***************** SEARCH MATCH *********************");
        executor.execute(JavaApiDemo::searchApiMatchExample);
        System.out.println("*********** SEARCH MATCH WITH FILTER ***************");
        executor.execute(JavaApiDemo::searchApiMatchWithFilterExample);
    }

    private static void getApiExample(TransportClient client) throws IOException {
        GetResponse response = client.prepareGet(INDEX_NAME, TYPE_NAME, String.valueOf(44)).get();
        System.out.println("Found user: " + response.getSourceAsMap().get("user"));
        System.out.println("\tVersion: " + response.getVersion());
        System.out.println("\t" + response);
    }

    private static void searchApiTermExample(TransportClient client) throws IOException {
        SearchResponse response = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("user", "test99"))
                .setExplain(true)
                .get();
        response.getHits().forEach(JavaApiDemo::printHit);
    }

    private static void searchApiMatchExample(TransportClient client) throws IOException {
        SearchResponse response = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery("message", "Java client"))
                .setFrom(0).setSize(5)
                .setExplain(true)
                .get();
        response.getHits().forEach(JavaApiDemo::printHit);
    }

    private static void searchApiMatchWithFilterExample(TransportClient client) throws IOException {
        SearchResponse response = client.prepareSearch(INDEX_NAME)
                .setTypes(TYPE_NAME)
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.matchQuery("message", "Java client"))
                .setPostFilter(QueryBuilders.termsQuery("user", asList("test1", "test2", "test3", "test4")))
                .setExplain(true)
                .get();
        response.getHits().forEach(JavaApiDemo::printHit);
    }

    private static void cleanupIndex(TransportClient client) throws Exception {
        for (int i = 0; i < COUNT; i++) {
            client.prepareDelete(INDEX_NAME, TYPE_NAME, String.valueOf(i)).execute();
        }
    }

    private static void populateData(TransportClient client) throws Exception {
        for (int i = 0; i < COUNT; i++) {
            createUser(client, i, "Test" + i);
        }
    }

    private static void createUser(TransportClient client, int id, String name) throws Exception {
        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("user", name)
                .field("postDate", LocalDate.now())
                .field("message", MESSAGES.get(id % MESSAGES.size()))
                .endObject();

        client.prepareIndex(INDEX_NAME, TYPE_NAME, String.valueOf(id))
                .setSource(builder)
                .execute();
    }

    private static void printHit(SearchHit hit) {
        System.out.println("[Hit] user: " + hit.getSourceAsMap().get("user"));
        System.out.println("\tScore: " + hit.getScore());
        //System.out.println("\tExplanation: " + hit.getExplanation().getDescription());
        System.out.println("\t" + hit.getSourceAsString());
    }
}
