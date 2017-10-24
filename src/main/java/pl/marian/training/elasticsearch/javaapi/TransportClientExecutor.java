package pl.marian.training.elasticsearch.javaapi;

import pl.marian.training.elasticsearch.javaapi.config.TransportClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.transport.TransportClient;

@Slf4j
public class TransportClientExecutor implements ElasticSearchOperationExecutor {

    private TransportClientConfig config = new TransportClientConfig();

    @Override
    public <T> T execute(ElasticOperation<T> action) {
        try (TransportClient client = config.createTransportClient()) {
            return action.execute(client);
        } catch (Exception e) {
            log.error("Exception while executing operation", e);
            e.printStackTrace();
            return null;
        }
    }
}
