package pl.marian.training.elasticsearch.javaapi.config;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class TransportClientConfig {

    public TransportClient createTransportClient() {
        try {
            return new PreBuiltTransportClient(createClientSettings())
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    private Settings createClientSettings() {
        return Settings.builder()
                //.put("cluster.name", "myClusterName")
                .put("client.transport.sniff", true)
                .put("client.transport.ping_timeout", new TimeValue(10, TimeUnit.SECONDS))
                .build();
    }
}
