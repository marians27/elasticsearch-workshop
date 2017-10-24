package pl.marian.training.elasticsearch.javaapi;

import org.elasticsearch.client.transport.TransportClient;

@FunctionalInterface
public interface ElasticVoidOperation {

    void execute(TransportClient client) throws Exception;

}
