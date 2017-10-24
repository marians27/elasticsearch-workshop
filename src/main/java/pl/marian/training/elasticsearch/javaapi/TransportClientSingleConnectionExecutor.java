package pl.marian.training.elasticsearch.javaapi;

import pl.marian.training.elasticsearch.javaapi.config.TransportClientConfig;
import org.elasticsearch.client.transport.TransportClient;

public class TransportClientSingleConnectionExecutor implements ElasticSearchOperationExecutor {

    private TransportClient client = new TransportClientConfig().createTransportClient();

    @Override
    public <T> T execute(ElasticOperation<T> action) {
        try {
            return action.execute(client);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
