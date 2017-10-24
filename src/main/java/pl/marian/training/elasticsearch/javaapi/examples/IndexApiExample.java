package pl.marian.training.elasticsearch.javaapi.examples;

import pl.marian.training.elasticsearch.javaapi.TransportClientExecutor;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.util.concurrent.Future;

import static pl.marian.training.elasticsearch.javaapi.examples.JavaApiExamplesConfig.INDEX_NAME;
import static pl.marian.training.elasticsearch.javaapi.examples.JavaApiExamplesConfig.TYPE_NAME;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class IndexApiExample {

    public static void main(String[] args) throws Exception {
        TransportClientExecutor executor = new TransportClientExecutor();

        Future<IndexResponse> future = executor.execute(IndexApiExample::addDocumentAsync);

        future.get();
    }

    private static Future<IndexResponse> addDocumentAsync(TransportClient client) throws IOException {
        XContentBuilder builder = jsonBuilder()
                .startObject()
                .field("user", "RDF")
                .field("postDate", LocalDate.now())
                .field("message", "trying out Elasticsearch")
                .endObject();

        return client.prepareIndex(INDEX_NAME, TYPE_NAME, "1")
                .setSource(builder)
                .execute();
    }
}
