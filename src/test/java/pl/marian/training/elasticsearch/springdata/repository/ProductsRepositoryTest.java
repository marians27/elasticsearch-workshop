package pl.marian.training.elasticsearch.springdata.repository;

import pl.marian.training.elasticsearch.springdata.configuration.SpringDataEsConfiguration;
import pl.marian.training.elasticsearch.springdata.model.CountryCode;
import pl.marian.training.elasticsearch.springdata.model.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static pl.marian.training.elasticsearch.springdata.model.CountryCode.GERMANY;
import static pl.marian.training.elasticsearch.springdata.model.CountryCode.UNITED_KINGDOM;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = SpringDataEsConfiguration.class)
public class ProductsRepositoryTest {

    @Autowired
    private ProductsRepository productsRepository;

    @Before
    public void populateTestData() {
        //TODO: Delete all products from index
        populate();
    }

    @Test
    public void addDocument() {
        Product toBeInserted = product(123L, "Aspirin", "", GERMANY, 12L);

        //TODO: add  toBeInserted to the index
        Product insertedProduct = null;

        assertThat(insertedProduct.getId()).isNotEmpty();
    }

    @Test
    public void searchForPanadol() {
        String productName = "Panadol";
        //TODO: call method to search single product by name
        Product panadol = null;

        assertThat(panadol).isNotNull();
        assertThat(panadol.getName()).isEqualTo("Panadol");
        assertThat(panadol.getWeight()).isEqualTo(15);
        assertThat(panadol.getCountryCode()).isEqualTo(UNITED_KINGDOM);
    }

    @Test
    public void searchForNurofenInGermany() {
        String productName = "Nurofen";
        String countryCode = GERMANY.getCode();

        //TODO: call method from repository to search single product by name and countryCode
        Product nurofen = null;

        assertThat(nurofen).isNotNull();
        assertThat(nurofen.getName()).isEqualTo("Nurofen");
        assertThat(nurofen.getWeight()).isEqualTo(30);
        assertThat(nurofen.getCountryCode()).isEqualTo(GERMANY);
    }

    @Test
    public void searchAllWithWeightBiggerOrEqual20() {
        long weight = 20;
        //TODO: Call method from repository to search products with weight >= 20
        List<Product> products = null;

        assertThat(products.stream().map(Product::getName).collect(toList())).containsOnly(
                "Nurofen",
                "Gripex",
                "Stoperan");
    }

    @Test
    public void searchMatchingDescriptionQuery() {
        String desc = "pain headache";

        //TODO: Call method from repository to search products matching description desc

        List<Product> products = null;

        assertThat(products.stream().map(Product::getName).collect(toList())).containsExactly(
                "Panadol",
                "Nurofen",
                "Nurofen",
                "Gripex",
                "Ibuprofen");
    }

    private Product product(Long productCode, String productName, String desc, CountryCode country, Long weight) {
        Product product = new Product();
        product.setProductCode(productCode);
        product.setName(productName);
        product.setCountryCode(country);
        product.setWeight(weight);
        product.setDescription(desc);
        return product;
    }

    private void populate() {
        List<Product> products = asList(
                product(200L, "Nurofen",
                        "For pain, fever",
                        GERMANY, 30L),
                product(201L, "Ibuprofen",
                        "Medication used for treating pain, fever, and inflammation",
                        GERMANY, 5L),
                product(202L, "Gripex",
                        "Paracetamol used for treating pain, fever, flu",
                        UNITED_KINGDOM, 20L),
                product(203L, "Panadol",
                        "Medication against headache",
                        UNITED_KINGDOM, 15L),
                product(204L, "Rutinoscorbin",
                        "Supports immunity, prevent colds and alleviates the symptoms of the common cold",
                        GERMANY, 12L),
                product(205L, "Nurofen",
                        "For pain, fever",
                        UNITED_KINGDOM, 19L),
                product(206L, "Stoperan",
                        "Drug which can be used in treatment of acute diarrhea",
                        UNITED_KINGDOM, 22L));

        //TODO: save all products
    }

}