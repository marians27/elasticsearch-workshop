package pl.marian.training.elasticsearch.javaapi.exercise;

import pl.marian.training.elasticsearch.javaapi.ElasticSearchOperationExecutor;
import pl.marian.training.elasticsearch.javaapi.TransportClientSingleConnectionExecutor;
import pl.marian.training.elasticsearch.springdata.model.CountryCode;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

import static java.util.stream.Collectors.toList;
import static org.elasticsearch.action.support.WriteRequest.RefreshPolicy.IMMEDIATE;

/*
 * JAVA API - exercise
 * 1)   Create new index es-workshop-4 and type products.
 *      Each product should have: numeric product code, name, country code and weight in gram
 * 2)   Implement empty methods in ProductService to search:
 *      a)  by name
 *      b)  by name and country code
 *      c)  by weight >=
 *      d)  for products with description matching given parameter
 * 3) Run ProductServiceTest for verification
 *
 * See TODOs below
 */
public class ProductService {

    private static final String INDEX_NAME = "es-workshop-4";
    private static final String TYPE_NAME = "products";
    private final ElasticSearchOperationExecutor executor = new TransportClientSingleConnectionExecutor();

    public String add(ProductDto product) {

        return executor.execute(client -> {
            IndexResponse response = client.prepareIndex(INDEX_NAME, TYPE_NAME)
                    .setSource(product.toMap())
                    .setRefreshPolicy(IMMEDIATE)
                    .get();

            return response.getId();
        });
    }

    public boolean addAll(List<ProductDto> products) {

        return executor.execute(client -> {
            List<Future<IndexResponse>> futures = products.stream()
                    .map(product -> client.prepareIndex(INDEX_NAME, TYPE_NAME)
                            .setRefreshPolicy(IMMEDIATE)
                            .setSource(product.toMap()).execute())
                    .collect(toList());
            for (Future f : futures) {
                f.get();
            }
            return true;
        });
    }

    public long deleteAll() {

        return executor.execute(client -> {
            BulkByScrollResponse response = DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                    .filter(QueryBuilders.matchAllQuery())
                    .source(INDEX_NAME)
                    .get();
            return response.getDeleted();

        });
    }

    public ProductDto getById(String id) {

        return executor.execute(client -> {
            GetResponse response = client.prepareGet(INDEX_NAME, TYPE_NAME, id).get();

            return new ProductDto(response.getSource());
        });
    }

    public ProductDto searchWithName(String productName) {

        return executor.execute(client -> {
            SearchResponse response = null;
            //TODO: implement method to find product by name
            return getFirst(response.getHits());
        });
    }

    public ProductDto searchWithNameAndCountry(String productName, CountryCode countryCode) {

        return executor.execute(client -> {
            SearchResponse response = null;
            //TODO: implement method to find product by name and country
            return getFirst(response.getHits());
        });
    }

    public List<ProductDto> searchWithWeightGt(Long weight) {

        return executor.execute(client -> {
            SearchResponse response = null;
            //TODO: implement method to find by weight >=
            return asList(response.getHits());
        });
    }

    public List<ProductDto> searchWithMatchingDescription(String description) {

        return executor.execute(client -> {
            SearchResponse response = null;
            //TODO: implement method to find products with description matchung given parameter
            return asList(response.getHits());
        });
    }

    private ProductDto getFirst(SearchHits searchHits) {
        return Arrays.stream(searchHits.getHits())
                .map(SearchHit::getSource)
                .map(ProductDto::new)
                .findFirst()
                .orElse(null);
    }

    private List<ProductDto> asList(SearchHits searchHits) {
        return Arrays.stream(searchHits.getHits())
                .map(SearchHit::getSource)
                .map(ProductDto::new)
                .collect(toList());
    }
}
