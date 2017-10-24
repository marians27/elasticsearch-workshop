package pl.marian.training.elasticsearch.javaapi.exercise;

import pl.marian.training.elasticsearch.springdata.model.CountryCode;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductServiceTest {

    private ProductService productService = new ProductService();

    @Before
    public void populateTestData() {
        productService.deleteAll();
        populate();
    }

    @Test
    public void addDocument() {
        ProductDto toBeInserted = product(123L, "Aspirin", "", CountryCode.GERMANY, 12L);

        String id = productService.add(toBeInserted);

        assertThat(id).isNotEmpty();
        assertThat(productService.getById(id)).isNotNull();
    }

    @Test
    public void searchForPanadol() {
        ProductDto panadol = productService.searchWithName("Panadol");

        assertThat(panadol).isNotNull();
        assertThat(panadol.getName()).isEqualTo("Panadol");
        assertThat(panadol.getWeight()).isEqualTo(15);
        Assertions.assertThat(panadol.getCountryCode()).isEqualTo(CountryCode.UNITED_KINGDOM);
    }

    @Test
    public void searchForNurofenInGermany() {
        ProductDto nurofen = productService.searchWithNameAndCountry("Nurofen", CountryCode.GERMANY);

        assertThat(nurofen).isNotNull();
        assertThat(nurofen.getName()).isEqualTo("Nurofen");
        assertThat(nurofen.getWeight()).isEqualTo(30);
        Assertions.assertThat(nurofen.getCountryCode()).isEqualTo(CountryCode.GERMANY);
    }

    @Test
    public void searchAllWithWeightBiggerOrEqual20() {
        List<ProductDto> products = productService.searchWithWeightGt(20L);

        assertThat(products.stream().map(ProductDto::getName).collect(toList())).containsOnly(
                "Nurofen",
                "Gripex",
                "Stoperan");
    }

    @Test
    public void searchMatchingDescriptionQuery() {
        List<ProductDto> products = productService.searchWithMatchingDescription("pain headache");

        assertThat(products.stream().map(ProductDto::getName).collect(toList())).containsExactly(
                "Panadol",
                "Nurofen",
                "Nurofen",
                "Gripex",
                "Ibuprofen");
    }

    private ProductDto product(Long productCode, String productName, String desc,
                               CountryCode country, Long weight) {
        ProductDto product = new ProductDto();
        product.setProductCode(productCode);
        product.setName(productName);
        product.setCountryCode(country);
        product.setWeight(weight);
        product.setDescription(desc);
        return product;
    }

    private void populate() {
        productService.addAll(asList(
                product(200L, "Nurofen",
                        "For pain, fever",
                        CountryCode.GERMANY, 30L),
                product(201L, "Ibuprofen",
                        "Medication used for treating pain, fever, and inflammation",
                        CountryCode.GERMANY, 5L),
                product(202L, "Gripex",
                        "Paracetamol used for treating pain, fever, flu",
                        CountryCode.UNITED_KINGDOM, 20L),
                product(203L, "Panadol",
                        "Medication against headache",
                        CountryCode.UNITED_KINGDOM, 15L),
                product(204L, "Rutinoscorbin",
                        "Supports immunity, prevent colds and alleviates the symptoms of the common cold",
                        CountryCode.GERMANY, 12L),
                product(205L, "Nurofen",
                        "For pain, fever",
                        CountryCode.UNITED_KINGDOM, 19L),
                product(206L, "Stoperan",
                        "Drug which can be used in treatment of acute diarrhea",
                        CountryCode.UNITED_KINGDOM, 22L)
        ));
    }

}