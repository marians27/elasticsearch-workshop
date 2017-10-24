package pl.marian.training.elasticsearch.javaapi;

public interface ElasticSearchOperationExecutor {

    <T> T execute(ElasticOperation<T> action);

    default void execute(ElasticVoidOperation action) {
        execute(client -> {
            action.execute(client);
            return true;
        });
    }
}
