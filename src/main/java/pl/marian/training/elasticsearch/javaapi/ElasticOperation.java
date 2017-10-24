package pl.marian.training.elasticsearch.javaapi;

import org.elasticsearch.client.transport.TransportClient;

@FunctionalInterface
public interface ElasticOperation<T> {

    T execute(TransportClient client) throws Exception;

}
