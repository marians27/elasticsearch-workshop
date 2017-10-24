package pl.marian.training.elasticsearch.basic;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

public class JavaApiSandbox {

    public static void main(String[] args) throws Exception {
        try (TransportClient client = createTransportClient()) {
            XContentBuilder builder = jsonBuilder()
                    .startObject()
                    .field("user", "RDF")
                    .field("postDate", LocalDate.now())
                    .field("message", "trying out Elasticsearch")
                    .endObject();

            IndexResponse response = client.prepareIndex("rdf-java-api-01", "user", "1")
                    .setSource(builder)
                    .get();
            System.out.println(response);
        }
    }

    private static TransportClient createTransportClient() throws UnknownHostException {
        return new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
    }
}
