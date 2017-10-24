package pl.marian.training.elasticsearch.springdata.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
//TODO: Make it ElasticSearch document
public class Product {

    @Id
    @JsonIgnore
    private String id;

    private Long productCode;

    private String name;

    private String description;

    private CountryCode countryCode;

    private Long weight;
}
