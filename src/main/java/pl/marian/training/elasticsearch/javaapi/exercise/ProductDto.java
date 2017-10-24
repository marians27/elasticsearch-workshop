package pl.marian.training.elasticsearch.javaapi.exercise;

import pl.marian.training.elasticsearch.springdata.model.CountryCode;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ProductDto {

    private Long productCode;

    private String name;

    private String description;

    private CountryCode countryCode;

    private Long weight;

    public ProductDto() {

    }

    public ProductDto(Map<String, Object> source) {
        this.productCode = Long.valueOf(source.get("productCode").toString());
        this.name = source.get("name").toString();
        this.description = source.get("description").toString();
        this.countryCode = CountryCode.convertFromCode(source.get("countryCode").toString());
        this.weight = Long.valueOf(source.get("weight").toString());
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("productCode", productCode);
        result.put("name", name);
        result.put("description", description);
        result.put("countryCode", countryCode.getCode());
        result.put("weight", weight);
        return result;
    }
}
